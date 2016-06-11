package com.x7ff.steam.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.x7ff.steam.domain.Game;
import com.x7ff.steam.domain.repository.GameRepository;
import com.x7ff.steam.domain.GameSnapshot;
import com.x7ff.steam.domain.Player;
import com.x7ff.steam.domain.repository.PlayerRepositoryImpl;
import com.x7ff.steam.domain.steam.SteamGame;
import com.x7ff.steam.domain.steam.SteamProfileOwnedGames;
import com.x7ff.steam.domain.steam.SteamProfileOwnedGamesResponse;
import com.x7ff.steam.service.steam.SteamOwnedGamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SteamPlayerService {
	// todo: implement daily query limits
	private static final int DAILY_API_QUERY_LIMIT = 100_000;

	private final PlayerRepositoryImpl playerRepository;
	private final GameRepository gameRepository;
	private final SteamOwnedGamesService steamOwnedGamesService;

	private final EntityManager entityManager;

	@Autowired
	public SteamPlayerService(PlayerRepositoryImpl playerRepository,
	                          GameRepository gameRepository,
	                          SteamOwnedGamesService steamOwnedGamesService,
	                          EntityManager entityManager) {
		this.playerRepository = playerRepository;
		this.gameRepository = gameRepository;
		this.steamOwnedGamesService = steamOwnedGamesService;
		this.entityManager = entityManager;
	}

	@Transactional
	public Player fetchPlayer(Player player, boolean save, String identifier) {
		SteamProfileOwnedGames steamProfile = steamOwnedGamesService.fetch(identifier);
		SteamProfileOwnedGamesResponse response = steamProfile.getResponse();
		if (response == null) {
			return null;
		}

		LocalDateTime time = LocalDateTime.now();
		if (player == null) {
			player = new Player(identifier);
			player.setCreationTime(time);
		}
		player.setIdentifier(identifier);
		player.setGameCount(response.getGameCount());
		player.setLastUpdated(time);

		if (player.getGameCount() > 0) {
			List<GameSnapshot> snapshots = Lists.newArrayList();

			player.getGames().clear();
			for (SteamGame steamGame : response.getGames()) {
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

		if (save) {
			gameRepository.persist(player.getGames());
			entityManager.merge(player);
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

	@Transactional
	public void resolveDisplayName(Player player) {
		resolveDisplayNames(Lists.newArrayList(player));
	}

	@Transactional
	public void resolveDisplayNames(List<Player> player) {

	}

}