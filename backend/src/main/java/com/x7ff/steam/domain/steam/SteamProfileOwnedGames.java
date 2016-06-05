package com.x7ff.steam.domain.steam;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SteamProfileOwnedGames {

	@JsonProperty("response")
	@Valid
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