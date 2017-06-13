package com.x7ff.steam.updater.batch.snapshots;

import javax.inject.Inject;

import com.x7ff.steam.shared.domain.Player;
import com.x7ff.steam.shared.service.steam.FetchOption;
import com.x7ff.steam.shared.service.steam.SteamPlayerService;
import com.x7ff.steam.updater.config.UpdaterConfig;
import org.springframework.batch.item.ItemProcessor;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class PlayerSnapshotUpdateProcessor implements ItemProcessor<Player, PlayerUpdate> {

	private static final Logger logger = Logger.getLogger(PlayerSnapshotUpdateProcessor.class.getSimpleName());

	private final SteamPlayerService steamPlayerService;

	private final long updateInterval;

	@Inject
	public PlayerSnapshotUpdateProcessor(UpdaterConfig updaterConfig, SteamPlayerService steamPlayerService) {
		this.steamPlayerService = steamPlayerService;
		this.updateInterval = updaterConfig.getSnapshotUpdateInterval();
	}

	@Override
	public PlayerUpdate process(Player player) throws Exception {
		if (!player.updateNeeded(updateInterval)) {
			return new PlayerUpdate(player, false);
		}
		Player result;
		try {
			result = steamPlayerService.fetchPlayer(player, player.getIdentifier(), FetchOption.RESOLVE_PROFILE);
		} catch (Exception e) {
			logger.log(Level.WARNING, "Failed to fetch player snapshot", e);
			result = null;
		}
		return new PlayerUpdate(result, result != null);
	}

}