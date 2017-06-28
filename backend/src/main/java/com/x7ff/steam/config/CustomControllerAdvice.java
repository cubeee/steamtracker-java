package com.x7ff.steam.config;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.inject.Inject;

@ControllerAdvice
public final class CustomControllerAdvice {

    @Inject
    private Environment environment;

    @Inject
    private BackendConfig backendConfig;

    @ModelAttribute(value = "developmentMode")
    public boolean isDevelopmentMode() {
        return environment.acceptsProfiles("development");
    }

    @ModelAttribute(value = "fillTables")
    public boolean fillTables() {
        return backendConfig.getFrontPage().isFillTables();
    }

    @ModelAttribute(value = "gamesInTables")
    public int gamesInTables() {
        return backendConfig.getFrontPage().getGamesInTables();
    }

    @ModelAttribute(value = "googleAnalyticsId")
    public String googleAnalyticsId() {
        return backendConfig.getGoogleAnalyticsId();
    }

}