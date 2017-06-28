package com.x7ff.steam.shared.service.steam;

import com.google.common.base.Joiner;
import com.x7ff.steam.shared.config.SteamConfig;
import com.x7ff.steam.shared.domain.steam.SteamProfiles;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public final class SteamProfileService extends SteamService<SteamProfiles> {

    @Inject
    public SteamProfileService(SteamConfig steamConfig) {
        super(steamConfig);
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