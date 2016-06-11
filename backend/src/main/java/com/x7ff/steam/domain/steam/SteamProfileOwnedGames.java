package com.x7ff.steam.domain.steam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamProfileOwnedGames {

	@JsonProperty("response")
	private SteamProfileOwnedGamesResponse response;

	@JsonProperty("response")
	public SteamProfileOwnedGamesResponse getResponse() {
		return response;
	}

	@JsonProperty("response")
	public void setResponse(SteamProfileOwnedGamesResponse response) {
		this.response = response;
	}

	public String toString() {
		return "SteamProfileRequest[response='" + response.toString() + "']";
	}

}