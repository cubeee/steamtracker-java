package com.x7ff.steam.shared.service.steam;

import com.x7ff.steam.shared.config.SteamConfig;
import com.x7ff.steam.shared.domain.steam.SteamProfileOwnedGames;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public final class SteamOwnedGamesService extends SteamService<SteamProfileOwnedGames> {

    @Inject
    public SteamOwnedGamesService(SteamConfig steamConfig) {
        super(steamConfig);
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
        return "&steamid=" + identifiers[0] + "&format=json&include_appinfo=1&include_played_free_games=1";
    }

}