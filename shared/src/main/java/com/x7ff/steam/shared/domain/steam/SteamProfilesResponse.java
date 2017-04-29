package com.x7ff.steam.shared.domain.steam;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamProfilesResponse {

	@JsonProperty("players")
	private List<SteamProfile> profiles;

	@JsonProperty("players")
	public List<SteamProfile> getProfiles() {
		return profiles;
	}

	@JsonProperty("players")
	public void setProfiles(List<SteamProfile> profiles) {
		this.profiles = profiles;
	}

}