package com.x7ff.steam.domain;

public final class MostPlayedGame {

	private final String name;

	private final String logoUrl;

	private final long minutesPlayed;

	public MostPlayedGame(String name, String logoUrl, long minutesPlayed) {
		this.name = name;
		this.logoUrl = logoUrl;
		this.minutesPlayed = minutesPlayed;
	}

	public String getName() {
		return name;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public long getMinutesPlayed() {
		return minutesPlayed;
	}

}