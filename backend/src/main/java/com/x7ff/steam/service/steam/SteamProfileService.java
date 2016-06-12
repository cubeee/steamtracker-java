package com.x7ff.steam.service.steam;

import com.google.common.base.Joiner;
import com.x7ff.steam.config.SteamTrackerConfig;
import com.x7ff.steam.domain.steam.SteamProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class SteamProfileService extends SteamService<SteamProfiles> {

	@Autowired
	public SteamProfileService(SteamTrackerConfig steamTrackerConfig) {
		super(steamTrackerConfig);
	}

	@Override
	public SteamProfiles fetch(String... args) {
		return fetchRestTemplate(SteamProfiles.class, getParameters(args));
	}

	@Override
	protected String getInterfaceName() {
		return "ISteamUser";
	}

	@Override
	protected String getCallName() {
		return "GetPlayerSummaries";
	}

	@Override
	protected String getVersion() {
		return "v0002";
	}

	protected String getParameters(String... identifiers) {
		return "&steamids=" + Joiner.on(",").join(identifiers);
	}

}