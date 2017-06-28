package com.x7ff.steam.shared.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
@Validated
public class SteamConfig {

    /**
     * Key for fetching information from Steam API
     */
    @NotNull
    @Size(min = 32, max = 32)
    @Value("${steam.api-key}")
    private String apiKey;

    @NotNull
    public String getApiKey() {
        return apiKey;
    }

}
