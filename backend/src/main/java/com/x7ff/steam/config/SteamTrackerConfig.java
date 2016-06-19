package com.x7ff.steam.config;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "steamtracker")
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
	 * Minutes between automatic player updates
	 */
	@Min(1)
	private int automaticUpdateInterval;

	/**
	 * Minutes between manual player updates
	 */
	@Min(1)
	private int manualUpdateInterval;

	public int getManualUpdateInterval() {
		return manualUpdateInterval;
	}

	public void setManualUpdateInterval(int manualUpdateInterval) {
		this.manualUpdateInterval = manualUpdateInterval;
	}

	public int getAutomaticUpdateInterval() {
		return automaticUpdateInterval;
	}

	public void setAutomaticUpdateInterval(int automaticUpdateInterval) {
		this.automaticUpdateInterval = automaticUpdateInterval;
	}

	public SteamConfig getSteam() {
		return steam;
	}

	public void setSteam(SteamConfig steam) {
		this.steam = steam;
	}

	public FrontPageConfig getFrontPage() {
		return frontPage;
	}

	public void setFrontPage(FrontPageConfig frontPage) {
		this.frontPage = frontPage;
	}

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

		public void setApiKey(String apiKey) {
			this.apiKey = apiKey;
		}

	}

	/**
	 * Front page related configuration
	 */
	public class FrontPageConfig {

		/**
		 * Number of games to show in tables
		 */
		@Size(min=1)
		private int gamesInTables = 10;

		/**
		 * Create empty table cells where the amount of games is less than <i>gamesInTables</i>
		 */
		private boolean fillTables;

		public int getGamesInTables() {
			return gamesInTables;
		}

		public void setGamesInTables(int gamesInTables) {
			this.gamesInTables = gamesInTables;
		}

		public boolean fillTables() {
			return fillTables;
		}

		public void setFillTables(boolean fillTables) {
			this.fillTables = fillTables;
		}

	}

}