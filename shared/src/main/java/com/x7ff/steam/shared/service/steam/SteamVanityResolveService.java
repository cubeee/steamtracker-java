package com.x7ff.steam.shared.service.steam;

import com.x7ff.steam.shared.config.SteamConfig;
import com.x7ff.steam.shared.domain.steam.SteamVanityResolve;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public final class SteamVanityResolveService extends SteamService<SteamVanityResolve> {

    @Inject
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