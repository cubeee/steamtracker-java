package com.x7ff.steam.service.steam;

import com.google.common.base.Joiner;
import com.x7ff.steam.config.SteamTrackerConfig;
import com.x7ff.steam.domain.steam.SteamProfileOwnedGames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class SteamOwnedGamesService extends SteamService<SteamProfileOwnedGames> {

	@Autowired
	public SteamOwnedGamesService(SteamTrackerConfig steamTrackerConfig) {
		super(steamTrackerConfig);
	}

	@Override
	public SteamProfileOwnedGames fetch(String... args) {
		return fetchRestTemplate(SteamProfileOwnedGames.class, getParameters(args));
	}

	@Override
	protected String getInterfaceName() {
		return "IPlayerService";
	}

	@Override
	protected String getCallName() {
		return "GetOwnedGames";
	}

	protected String getParameters(String... identifiers) {
		return "&steamid=" + Joiner.on(",").join(identifiers) + "&format=json&include_appinfo=1&include_played_free_games=1";
	}

}