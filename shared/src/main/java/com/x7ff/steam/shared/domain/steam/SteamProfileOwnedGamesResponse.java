package com.x7ff.steam.shared.domain.steam;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamProfileOwnedGamesResponse {

	@JsonProperty(value = "game_count")
	private int gameCount;

	@JsonProperty("games")
	private List<SteamGame> games;

	@JsonProperty("game_count")
	public int getGameCount() {
		return gameCount;
	}

	@JsonProperty("game_count")
	public void setGameCount(int gameCount) {
		this.gameCount = gameCount;
	}

	@JsonProperty("games")
	public List<SteamGame> getGames() {
		return games;
	}

	@JsonProperty("games")
	public void setGames(List<SteamGame> games) {
		this.games = games;
	}

	@Override
	public String toString() {
		return "SteamProfileResponse[gameCount='" + gameCount + "', games='" + games + "']";
	}

}