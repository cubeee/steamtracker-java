package com.x7ff.steam.shared.config;

import java.util.TimeZone;
import javax.annotation.PostConstruct;
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
@EnableConfigurationProperties(SharedConfig.class)
@Getter
@Setter
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
    @Getter
    @Setter
    public class SteamConfig {

        /**
         * Key for fetching information from Steam API
         */
        @NotNull
        @Size(min = 32, max = 32)
        private String apiKey;

    }

}