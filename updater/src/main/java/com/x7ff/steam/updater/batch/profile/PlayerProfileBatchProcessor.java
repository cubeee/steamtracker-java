package com.x7ff.steam.updater.batch.profile;

import java.util.List;
import javax.inject.Inject;

import com.x7ff.steam.shared.domain.Player;
import com.x7ff.steam.shared.service.steam.SteamPlayerService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class PlayerProfileBatchProcessor implements ItemProcessor<List<Player>, List<Player>> {

	@Inject
	private SteamPlayerService steamPlayerService;

	@Override
	public List<Player> process(List<Player> players) throws Exception {
		steamPlayerService.resolveProfiles(players);
		return players;
	}

}