package com.x7ff.steam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Configuration
@ConfigurationProperties(prefix = "steamtracker")
@EnableConfigurationProperties
@Validated
public class BackendConfig {

    /**
     * Front page related configuration
     */
    @NotNull
    private FrontPageConfig frontPage = new FrontPageConfig();

    /**
     * Front page related configuration
     */
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

        public int getGamesInTables() {
            return gamesInTables;
        }

        public boolean isFillTables() {
            return fillTables;
        }
    }

    public FrontPageConfig getFrontPage() {
        return frontPage;
    }

    @NotNull
    @Value("${google-analytics-id: }")
    private String googleAnalyticsId = "";

    public String getGoogleAnalyticsId() {
        return googleAnalyticsId;
    }

}
