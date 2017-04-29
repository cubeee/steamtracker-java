package com.x7ff.steam.shared.domain.steam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamProfiles {

	@JsonProperty("response")
	private SteamProfilesResponse response;

	@JsonProperty("response")
	public SteamProfilesResponse getResponse() {
		return response;
	}

	@JsonProperty("response")
	public void setResponse(SteamProfilesResponse response) {
		this.response = response;
	}

}