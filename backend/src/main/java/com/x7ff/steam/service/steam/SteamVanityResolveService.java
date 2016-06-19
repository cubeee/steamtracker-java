package com.x7ff.steam.service.steam;

import javax.inject.Inject;

import com.x7ff.steam.config.SteamTrackerConfig;
import com.x7ff.steam.domain.steam.SteamVanityResolve;
import org.springframework.stereotype.Service;

@Service
public final class SteamVanityResolveService extends SteamService<SteamVanityResolve> {

	@Inject
	public SteamVanityResolveService(SteamTrackerConfig steamTrackerConfig) {
		super(steamTrackerConfig);
	}

	@Override
	public SteamVanityResolve fetch(String... names) {
		return fetchRestTemplate(SteamVanityResolve.class, "&vanityurl=" + names[0]);
	}

	@Override
	protected String getInterfaceName() {
		return "ISteamUser";
	}

	@Override
	protected String getCallName() {
		return "ResolveVanityURL";
	}

}