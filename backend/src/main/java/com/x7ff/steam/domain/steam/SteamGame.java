package com.x7ff.steam.domain.steam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamGame {

	@JsonProperty("appid")
	private int appId;

	@JsonProperty("name")
	private String name;

	@JsonProperty("playtime_forever")
	private int minutesPlayed;

	@JsonProperty("img_icon_url")
	private String imageIconUrl;

	@JsonProperty("img_logo_url")
	private String imageLogoUrl;

	@JsonProperty("has_community_visible_stats")
	private boolean hasCommunityVisibleStats;

	@JsonProperty("appid")
	public int getAppId() {
		return appId;
	}

	@JsonProperty("appid")
	public void setAppId(int appId) {
		this.appId = appId;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("playtime_forever")
	public int getMinutesPlayed() {
		return minutesPlayed;
	}

	@JsonProperty("playtime_forever")
	public void setMinutesPlayed(int minutesPlayed) {
		this.minutesPlayed = minutesPlayed;
	}

	@JsonProperty("img_icon_url")
	public String getImageIconUrl() {
		return imageIconUrl;
	}

	@JsonProperty("img_icon_url")
	public void setImageIconUrl(String imageIconUrl) {
		this.imageIconUrl = imageIconUrl;
	}

	@JsonProperty("img_logo_url")
	public String getImageLogoUrl() {
		return imageLogoUrl;
	}

	@JsonProperty("img_logo_url")
	public void setImageLogoUrl(String imageLogoUrl) {
		this.imageLogoUrl = imageLogoUrl;
	}

	@JsonProperty("has_community_visible_stats")
	public boolean isHasCommunityVisibleStats() {
		return hasCommunityVisibleStats;
	}

	@JsonProperty("has_community_visible_stats")
	public void setHasCommunityVisibleStats(boolean hasCommunityVisibleStats) {
		this.hasCommunityVisibleStats = hasCommunityVisibleStats;
	}

	@Override
	public String toString() {
		return "SteamGame[" +
				"appId='" + appId +
				"', name='" + name + '\'' +
				"', minutesPlayed='" + minutesPlayed +
				"', imageIconUrl='" + imageIconUrl +
				"', imageLogoUrl='" + imageLogoUrl +
				"', hasCommunityVisibleStats='" + hasCommunityVisibleStats +
				"']";
	}
}