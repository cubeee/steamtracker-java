package com.x7ff.steam.batch.snapshots;

import javax.inject.Inject;

import com.x7ff.steam.config.SteamTrackerConfig;
import com.x7ff.steam.domain.Player;
import com.x7ff.steam.service.steam.SteamPlayerService;
import org.springframework.batch.item.ItemProcessor;

public final class PlayerSnapshotUpdateProcessor implements ItemProcessor<Player, PlayerUpdate> {

	private final SteamPlayerService steamPlayerService;

	private final long updateInterval;

	@Inject
	public PlayerSnapshotUpdateProcessor(SteamTrackerConfig steamTrackerConfig, SteamPlayerService steamPlayerService) {
		this.steamPlayerService = steamPlayerService;
		this.updateInterval = steamTrackerConfig.getUpdater().getSnapshotUpdateInterval();
	}

	@Override
	public PlayerUpdate process(Player player) throws Exception {
		if (!player.updateNeeded(updateInterval)) {
			return new PlayerUpdate(player, false);
		}
		Player result = steamPlayerService.fetchPlayer(player, false, player.getIdentifier());
		return new PlayerUpdate(player, result != null);
	}

}