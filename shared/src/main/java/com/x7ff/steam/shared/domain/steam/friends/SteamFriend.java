package com.x7ff.steam.shared.domain.steam.friends;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamFriend {

	@JsonProperty("steamid")
	private String steamId;

	@JsonProperty("relationship")
	private String relationship;

	@JsonProperty("friend_since")
	private long friendSince;

	@JsonProperty("steamid")
	public String getSteamId() {
		return steamId;
	}

	@JsonProperty("steamid")
	public void setSteamId(String steamId) {
		this.steamId = steamId;
	}

	@JsonProperty("relationship")
	public String getRelationship() {
		return relationship;
	}

	@JsonProperty("relationship")
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	@JsonProperty("friend_since")
	public long getFriendSince() {
		return friendSince;
	}

	@JsonProperty("friend_since")
	public void setFriendSince(long friendSince) {
		this.friendSince = friendSince;
	}

}