package com.x7ff.steam.service.steam;

import javax.inject.Inject;

import com.x7ff.steam.config.SteamTrackerConfig;
import com.x7ff.steam.domain.steam.friends.SteamFriends;
import org.springframework.stereotype.Service;

@Service
public final class SteamFriendListService extends SteamService<SteamFriends> {

	@Inject
	public SteamFriendListService(SteamTrackerConfig steamTrackerConfig) {
		super(steamTrackerConfig);
	}

	@Override
	public SteamFriends fetch(String... args) {
		return fetchRestTemplate(SteamFriends.class, getParameters(args));
	}

	@Override
	protected String getInterfaceName() {
		return "ISteamUser";
	}

	@Override
	protected String getCallName() {
		return "GetFriendList";
	}

	protected String getParameters(String... identifiers) {
		return "&steamid=" + identifiers[0];
	}

}