package com.x7ff.steam.batch.snapshots;

import java.util.List;
import javax.inject.Inject;

import com.google.common.collect.Lists;
import com.x7ff.steam.domain.Player;
import com.x7ff.steam.domain.repository.GameRepository;
import com.x7ff.steam.domain.repository.PlayerRepository;
import com.x7ff.steam.domain.repository.statistics.PlayerGameStatistics;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.stereotype.Component;

@Component
public final class PlayerWriter extends JpaItemWriter<PlayerUpdate> {

	@Inject
	private PlayerRepository playerRepository;

	@Inject
	private GameRepository gameRepository;

	@Inject
	private PlayerGameStatistics playerGameStatistics;

	@Override
	public void write(List<? extends PlayerUpdate> updates) {
		List<Player> players = Lists.newArrayList();
		for (PlayerUpdate update : updates) {
			if (!update.isUpdated()) {
				continue;
			}
			Player player = update.getPlayer();
			players.add(player);
			gameRepository.persist(player.getGames());
		}
		playerRepository.save(players);

		for (Player player : players) {
			playerGameStatistics.evictPlayerCaches(player.getId());
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

}