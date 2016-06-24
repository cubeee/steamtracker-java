package com.x7ff.steam.config;

import java.util.concurrent.TimeUnit;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "steamtracker")
@EnableConfigurationProperties(SteamTrackerConfig.class)
@Getter
@Setter
public class SteamTrackerConfig {

	/**
	 * Steam related configuration
	 */
	@NotNull
	private SteamConfig steam = new SteamConfig();

	/**
	 * Front page related configuration
	 */
	@NotNull
	private FrontPageConfig frontPage = new FrontPageConfig();

	/**
	 * Updater related configuration
	 */
	@NotNull
	private UpdaterConfig updater = new UpdaterConfig();

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

	/**
	 * Front page related configuration
	 */
	@Getter
	@Setter
	public class FrontPageConfig {

		/**
		 * Number of games to show in tables.
		 */
		@Size(min=1)
		private int gamesInTables = 10;

		/**
		 * Create empty table cells where the amount of games is less than <i>gamesInTables</i>.
		 */
		private boolean fillTables;

	}

	/**
	 * Updater related configuration
	 */
	@Getter
	@Setter
	public class UpdaterConfig {

		/**
		 * Whether to enable manual player profile updating upon visitation. Having this enabled in production
		 * is not recommended as it can quickly exhaust api key limits especially with large player base and a
		 * frequent <i>manualUpdateInterval</i>. Turned off by default.
		 */
		private boolean enableManualUpdating;

		/**
		 * Delay between automatic player updates in minutes. Delay is started after the previous update has finished.
		 * Default interval is set at 12 hours.
		 */
		@Min(1)
		private long automaticUpdateInterval = TimeUnit.HOURS.toMinutes(6);

		/**
		 * Minimum minutes between manual player updates. Default interval is set at 60 minutes.
		 */
		@Min(1)
		private long manualUpdateInterval = TimeUnit.HOURS.toMinutes(1);

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

}