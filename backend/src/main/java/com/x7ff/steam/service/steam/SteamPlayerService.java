package com.x7ff.steam.service.steam;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.x7ff.steam.domain.Game;
import com.x7ff.steam.domain.GameSnapshot;
import com.x7ff.steam.domain.Player;
import com.x7ff.steam.domain.repository.GameRepository;
import com.x7ff.steam.domain.repository.PlayerRepository;
import com.x7ff.steam.domain.steam.SteamGame;
import com.x7ff.steam.domain.steam.SteamProfile;
import com.x7ff.steam.domain.steam.SteamProfileOwnedGames;
import com.x7ff.steam.domain.steam.SteamProfileOwnedGamesResponse;
import com.x7ff.steam.domain.steam.SteamProfilesResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

@Service
public class SteamPlayerService {
	private final Logger logger = Logger.getLogger(SteamPlayerService.class.getName());

	// todo: implement daily query limits
	private static final int DAILY_API_QUERY_LIMIT = 100_000;

	public static final int MAX_PROFILE_BATCH_SIZE = 100;

	private final EntityManager entityManager;
	private final PlayerRepository playerRepository;
	private final GameRepository gameRepository;
	private final SteamOwnedGamesService steamOwnedGamesService;
	private final SteamProfileService steamProfileService;

	@Inject
	public SteamPlayerService(EntityManager entityManager,
	                          PlayerRepository playerRepository,
	                          GameRepository gameRepository,
	                          SteamOwnedGamesService steamOwnedGamesService,
	                          SteamProfileService steamProfileService) {
		this.entityManager = entityManager;
		this.playerRepository = playerRepository;
		this.gameRepository = gameRepository;
		this.steamOwnedGamesService = steamOwnedGamesService;
		this.steamProfileService = steamProfileService;
	}

	@Transactional
	public Player fetchPlayer(Player player, String identifier, FetchOption... options) {
		SteamProfileOwnedGames steamProfile;
		try {
			steamProfile = steamOwnedGamesService.fetch(identifier);
		} catch (Exception e) {
			logger.log(Level.WARNING, "Failed to fetch player", e);
			return null;
		}
		if (steamProfile == null) {
			return null;
		}
		SteamProfileOwnedGamesResponse response = steamProfile.getResponse();
		if (response == null) {
			return null;
		}

		ZonedDateTime time = ZonedDateTime.now();
		if (player == null) {
			player = new Player(identifier);
			player.setCreationTime(time);
		}
		player.setIdentifier(identifier);
		player.setGameCount(response.getGameCount());
		player.setLastUpdated(time);

		if (optionEnabled(options, FetchOption.RESOLVE_PROFILE)) {
			resolveProfile(player);
		}

		List<SteamGame> steamGames = response.getGames();
		List<Game> games = Lists.newArrayList();

		if (steamGames == null || steamGames.isEmpty()) {
			return null;
		} else {
			List<GameSnapshot> snapshots = Lists.newArrayList();
			List<GameSnapshot> lastSnapshots = getLastSnapshots(player);

			for (SteamGame steamGame : response.getGames()) {
				Game game = Game.from(steamGame);
				games.add(game);

				if (steamGame.getMinutesPlayed() == 0) {
					continue;
				}
				GameSnapshot lastSnapshot = getLastSnapshot(lastSnapshots, steamGame);
				if (lastSnapshot != null && steamGame.getMinutesPlayed() <= lastSnapshot.getMinutesPlayed()) {
					continue;
				}

				GameSnapshot snapshot = GameSnapshot.from(player, game, steamGame, time);
				snapshots.add(snapshot);
			}

			if (!snapshots.isEmpty()) {
				Collections.sort(snapshots, (snapshot, other) -> {
					if (snapshot == null || other == null) {
						return 0;
					}
					return Ints.compare(other.getMinutesPlayed(), snapshot.getMinutesPlayed());
				});

				player.setSnapshots(snapshots);
			}
		}

		if (optionEnabled(options, FetchOption.SAVE_PLAYER)) {
			games = gameRepository.persist(games);
			player = entityManager.merge(player);
			entityManager.flush();
		}
		player.setGames(games);

		return player;
	}

	private boolean optionEnabled(FetchOption[] options, FetchOption option) {
		return Arrays.stream(options).anyMatch(o -> o == option);
	}

	public List<Player> fetchPlayers(String[] identifiers, FetchOption... options) {
		List<Player> players = Lists.newArrayList();
		List<Game> games = Lists.newArrayList();

		for (String identifier : identifiers) {
			try {
				Player player = fetchPlayer(null, identifier, options);
				if (player == null) {
					continue;
				}
				games.addAll(player.getGames());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		gameRepository.persist(games);
		if (!players.isEmpty()) {
			playerRepository.save(players);
		}
		return players;
	}

	public void resolveProfile(Player player) {
		SteamProfilesResponse response;
		try {
			response = steamProfileService.fetch(player.getIdentifier()).getResponse();
		} catch (RestClientException e) {
			logger.warning("Player profile for '" + player.getDisplayName() + "' couldn't be resolved: " + e.getMessage());
			return;
		} catch (Exception e) {
			logger.log(Level.WARNING, "Player profile for '" + player.getDisplayName() + "' couldn't be resolved", e);
			return;
		}
		List<SteamProfile> profiles = response.getProfiles();
		if (profiles.isEmpty()) {
			return;
		}
		SteamProfile profile = profiles.get(0);
		updatePlayerInformation(player, profile);
	}

	public void resolveProfiles(List<Player> players) throws Exception {
		if (players.size() > MAX_PROFILE_BATCH_SIZE) {
			throw new IllegalArgumentException("Maximum of " + MAX_PROFILE_BATCH_SIZE + " players' profiles can be resolved at once");
		}
		List<String> identifiers = players.stream().map(Player::getIdentifier).collect(Collectors.toList());
		SteamProfilesResponse response;
		try {
			response = steamProfileService.fetch(identifiers).getResponse();
		} catch (RestClientException e) {
			logger.warning("Batch profile resolve failed: " + e.getMessage());
			return;
		} catch (Exception e) {
			logger.log(Level.WARNING, "Batch profile resolve failed", e);
			return;
		}
		List<SteamProfile> profiles = response.getProfiles();
		if (profiles.isEmpty()) {
			return;
		}
		players.forEach(player -> {
			profiles.stream().filter(profile -> profile.getIdentifier().equals(player.getIdentifier())).forEach(profile -> {
				updatePlayerInformation(player, profile);
			});
		});
	}

	private void updatePlayerInformation(Player player, SteamProfile profile) {
		player.setName(profile.getName());
		player.setAvatar(profile.getAvatar());
		player.setAvatarMedium(profile.getAvatarMedium());
		player.setAvatarFull(profile.getAvatarFull());
		player.setCountryCode(profile.getCountryCode());
	}

	@SuppressWarnings("unchecked")
	private List<GameSnapshot> getLastSnapshots(Player player) {
		List<GameSnapshot> emptyList = Lists.newArrayList();
		if (!player.hasId()) {
			return emptyList;
		}
		Query query = entityManager.createNativeQuery(
				"SELECT DISTINCT ON(game_id) * FROM game_snapshot " +
						"WHERE player_id = :player_id ORDER BY game_id ASC, date DESC", GameSnapshot.class);
		query.setParameter("player_id", player.getId());
		try {
			return query.getResultList();
		} catch (Exception e) {
			logger.log(Level.WARNING, "Failed to get last snapshots", e);
		}
		return emptyList;
	}

	private GameSnapshot getLastSnapshot(List<GameSnapshot> snapshots, SteamGame game) {
		if (snapshots.isEmpty() || game == null) {
			return null;
		}
		for (GameSnapshot snapshot : snapshots) {
			if (snapshot.getGame().getAppId() == game.getAppId()) {
				return snapshot;
			}
		}
		return null;
	}

}