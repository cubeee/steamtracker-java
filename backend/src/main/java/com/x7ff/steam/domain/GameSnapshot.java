package com.x7ff.steam.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.x7ff.steam.domain.steam.SteamGame;

@Entity
public final class GameSnapshot {

	@Id
	@GeneratedValue
	private Long id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "player_id")
	private Player player;

	@ManyToOne(optional = false)
	@JoinColumn(name = "game_id")
	private Game game;

	@Column(name = "minutes_played", nullable = false)
	private int minutesPlayed;

	@Column(nullable = false)
	private LocalDateTime date;

	protected GameSnapshot() {

	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public int getMinutesPlayed() {
		return minutesPlayed;
	}

	public void setMinutesPlayed(int minutesPlayed) {
		this.minutesPlayed = minutesPlayed;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public static GameSnapshot from(Player player, Game game, SteamGame steamGame) {
		GameSnapshot snapshot = new GameSnapshot();
		snapshot.setPlayer(player);
		snapshot.setGame(game);
		snapshot.setMinutesPlayed(steamGame.getMinutesPlayed());
		snapshot.setDate(LocalDateTime.now(ZoneId.of("Europe/Paris")));
		return snapshot;
	}

}