package com.x7ff.steam.shared.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.TimeZone;

@Configuration
@ConfigurationProperties(prefix = "steamtracker")
@EnableConfigurationProperties(SharedConfig.class)
@Validated
public class SharedConfig {

    @NotNull
    private String timezone = "GMT";

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(timezone));
    }

    /**
     * Steam related configuration
     */
    @NotNull
    private SteamConfig steam = new SteamConfig();

    /**
     * Steam related configuration
     */
    public class SteamConfig {

        /**
         * Key for fetching information from Steam API
         */
        @NotNull
        @Size(min = 32, max = 32)
        private String apiKey;

        public String getApiKey() {
            return apiKey;
        }
    }

    public String getTimezone() {
        return timezone;
    }

    public SteamConfig getSteam() {
        return steam;
    }
}