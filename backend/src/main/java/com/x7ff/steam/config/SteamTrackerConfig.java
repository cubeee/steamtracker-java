package com.x7ff.steam.config;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "steamtracker")
@EnableConfigurationProperties(SteamTrackerConfig.class)
@Getter
@Setter
public final class SteamTrackerConfig {

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
	private UpdaterConfig updaterConfig = new UpdaterConfig();

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
		 * Minutes between automatic player updates.
		 */
		@Min(1)
		private int automaticUpdateInterval;

		/**
		 * Minutes between manual player updates.
		 */
		@Min(1)
		private int manualUpdateInterval;

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