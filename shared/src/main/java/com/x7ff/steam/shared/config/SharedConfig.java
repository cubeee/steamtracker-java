package com.x7ff.steam.shared.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.TimeZone;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "steamtracker")
@Validated
public class SharedConfig {

    @NotNull
    private String timezone = "GMT";

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(timezone));
    }

    public String getTimezone() {
        return timezone;
    }

}