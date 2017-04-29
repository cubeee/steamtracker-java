package com.x7ff.steam.shared.domain.steam.friends;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamFriendsList {

	@JsonProperty("friends")
	private List<SteamFriend> friends;

	@JsonProperty("friends")
	public List<SteamFriend> getFriends() {
		return friends;
	}

	@JsonProperty("friends")
	public void setFriends(List<SteamFriend> friends) {
		this.friends = friends;
	}

}