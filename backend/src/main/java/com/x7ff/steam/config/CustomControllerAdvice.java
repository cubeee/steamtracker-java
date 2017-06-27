package com.x7ff.steam.config;

import com.x7ff.steam.shared.config.SteamTrackerConfig;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.inject.Inject;

@ControllerAdvice
public final class CustomControllerAdvice {

    @Inject
    private Environment environment;

    @Inject
    private SteamTrackerConfig steamTrackerConfig;

    @Inject
    private BackendConfig backendConfig;

    @ModelAttribute(value = "developmentMode")
    public boolean isDevelopmentMode() {
        return environment.acceptsProfiles("development");
    }

    @ModelAttribute(value = "fillTables")
    public boolean fillTables() {
        return steamTrackerConfig.getFrontPage().isFillTables();
    }

    @ModelAttribute(value = "gamesInTables")
    public int gamesInTables() {
        return steamTrackerConfig.getFrontPage().getGamesInTables();
    }

    @ModelAttribute(value = "googleAnalyticsId")
    public String googleAnalyticsId() {
        return backendConfig.getGoogleAnalyticsId();
    }

}