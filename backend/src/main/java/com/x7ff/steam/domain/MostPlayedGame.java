package com.x7ff.steam.domain;

public final class MostPlayedGame {

	private final Game game;

	private final long minutesPlayed;

	public MostPlayedGame(Game game, long minutesPlayed) {
		this.game = game;
		this.minutesPlayed = minutesPlayed;
	}

	public Game getGame() {
		return game;
	}

	public long getMinutesPlayed() {
		return minutesPlayed;
	}

}