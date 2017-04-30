package com.x7ff.steam.updater.config;

import java.util.concurrent.TimeUnit;
import javax.validation.constraints.Min;

import com.x7ff.steam.shared.config.SteamTrackerConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties
@EnableConfigurationProperties({ SteamTrackerConfig.class, UpdaterConfig.class })
@Getter
@Setter
@Validated
public class UpdaterConfig {

	/**
	 * Flag for optionally disabling scheduled tasks from being started.
	 * May be useful in development environment when doing frequent restarts.
	 */
	private boolean disableScheduledTasks;

	/**
	 * Delay between automatic player updates in minutes. Delay is started after the previous update has finished.
	 * Default interval is set at 12 hours.
	 */
	@Min(1)
	private long automaticUpdateInterval = TimeUnit.HOURS.toMinutes(6);

	/**
	 * "Minimum age of player snapshots in minutes before they need to be updated.
	 * Default interval is set at 6 hours."
	 */
	@Min(1)
	private long snapshotUpdateInterval = TimeUnit.HOURS.toMinutes(6);

	/**
	 * The amount of players to fetch from database at once for snapshot updating.
	 */
	@Min(1)
	private int snapshotsPageSize = 100;

	/**
	 * The amount of players to process before saving for snapshot updating.
	 */
	@Min(1)
	private int snapshotsChunkSize = 100;

}