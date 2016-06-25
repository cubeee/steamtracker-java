package com.x7ff.steam.service.steam;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.persistence.EntityManager;

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

@Service
public class SteamPlayerService {
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
	public Player fetchPlayer(Player player, boolean save, String identifier) {
		SteamProfileOwnedGames steamProfile = steamOwnedGamesService.fetch(identifier);
		SteamProfileOwnedGamesResponse response = steamProfile.getResponse();
		if (response == null) {
			return null;
		}

		boolean newPlayer = false;

		ZonedDateTime time = ZonedDateTime.now();
		if (player == null) {
			player = new Player(identifier);
			player.setCreationTime(time);
			newPlayer = true;
		}
		player.setIdentifier(identifier);
		player.setGameCount(response.getGameCount());
		player.setLastUpdated(time);

		List<SteamGame> steamGames = response.getGames();
		if (steamGames.isEmpty()) {
			return null;
		} else {
			List<GameSnapshot> snapshots = Lists.newArrayList();

			player.getGames().clear();
			for (SteamGame steamGame : response.getGames()) {
				if (steamGame.getMinutesPlayed() == 0) {
					continue;
				}
				Game game = Game.from(steamGame);
				player.getGames().add(game);

				GameSnapshot snapshot = GameSnapshot.from(player, game, steamGame, time);
				snapshots.add(snapshot);
			}

			Collections.sort(snapshots, (snapshot, other) -> {
				if (snapshot == null || other == null) {
					return 0;
				}
				return Ints.compare(other.getMinutesPlayed(), snapshot.getMinutesPlayed());
			});

			player.getSnapshots().addAll(snapshots);
		}

		if (newPlayer) {
			player = resolveProfile(player);
		}

		if (save) {
			gameRepository.persist(player.getGames());
			player = entityManager.merge(player);
			entityManager.flush();
		}
		return player;
	}

	@Transactional
	public List<Player> fetchPlayers(String... identifiers) {
		List<Player> players = Lists.newArrayList();
		List<Game> games = Lists.newArrayList();

		for (String identifier : identifiers) {
			try {
				Player player = fetchPlayer(null, false, identifier);
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

	public Player resolveProfile(Player player) {
		SteamProfilesResponse response = steamProfileService.fetch(player.getIdentifier()).getResponse();
		List<SteamProfile> profiles = response.getProfiles();
		if (profiles.isEmpty()) {
			return player;
		}
		SteamProfile profile = profiles.get(0);
		updatePlayerInformation(player, profile);
		return player;
	}

	public void resolveProfiles(List<Player> players) throws Exception {
		if (players.size() > MAX_PROFILE_BATCH_SIZE) {
			throw new IllegalArgumentException("Maximum of " + MAX_PROFILE_BATCH_SIZE + " players' profiles can be resolved at once");
		}
		List<String> identifiers = players.stream().map(Player::getIdentifier).collect(Collectors.toList());
		SteamProfilesResponse response = steamProfileService.fetch(identifiers).getResponse();
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

}