package com.x7ff.steam.service.steam;

import com.x7ff.steam.config.SteamConfig;
import com.x7ff.steam.domain.steam.SteamVanityResolve;
import org.springframework.stereotype.Service;

@Service
public final class SteamVanityResolveService extends SteamService<SteamVanityResolve> {

	public SteamVanityResolveService(SteamConfig steamConfig) {
		super(steamConfig);
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