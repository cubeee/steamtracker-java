package com.x7ff.steam.updater.batch.snapshots;

import javax.inject.Inject;

import com.x7ff.steam.shared.config.SteamTrackerConfig;
import com.x7ff.steam.shared.domain.Player;
import com.x7ff.steam.shared.service.steam.FetchOption;
import com.x7ff.steam.shared.service.steam.SteamPlayerService;
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
		Player result = steamPlayerService.fetchPlayer(player, player.getIdentifier(), FetchOption.RESOLVE_PROFILE);
		return new PlayerUpdate(result, result != null);
	}

}