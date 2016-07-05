package com.x7ff.steam.domain.steam.friends;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamFriends {

	@JsonProperty("friendslist")
	private SteamFriendsList friendsList;

	@JsonProperty("friendslist")
	public SteamFriendsList getFriendsList() {
		return friendsList;
	}

	@JsonProperty("friendslist")
	public void setFriendsList(SteamFriendsList friendsList) {
		this.friendsList = friendsList;
	}

}