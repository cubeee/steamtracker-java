package com.x7ff.steam.shared.config;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "steamtracker")
@EnableConfigurationProperties(SteamTrackerConfig.class)
@Getter
@Setter
@Validated
public class SteamTrackerConfig {

    /**
     * Front page related configuration
     */
    @NotNull
    private FrontPageConfig frontPage = new FrontPageConfig();

    /**
     * Front page related configuration
     */
    @Getter
    @Setter
    public class FrontPageConfig {

        /**
         * Number of games to show in tables.
         */
        @Size(min = 1)
        private int gamesInTables = 10;

        /**
         * Create empty table cells where the amount of games is less than <i>gamesInTables</i>.
         */
        private boolean fillTables;

    }

}