package com.x7ff.steam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties
@EnableConfigurationProperties(BackendConfig.class)
@Validated
public class BackendConfig {

    @NotNull
    @Value("${google-analytics-id: }")
    private String googleAnalyticsId = "";

    public String getGoogleAnalyticsId() {
        return googleAnalyticsId;
    }

}
