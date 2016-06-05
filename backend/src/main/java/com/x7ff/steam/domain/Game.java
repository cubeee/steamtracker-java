package com.x7ff.steam.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.x7ff.steam.domain.steam.SteamGame;

@Entity
public final class Game {

	@Id
	@Column(name = "appId", unique = true)
	private int appId;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "logo_url", nullable = false)
	private String logoUrl;

	protected Game() {

	}

	public long getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setIconURL(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public static Game from(SteamGame steamGame) {
		Game game = new Game();
		game.setAppId(steamGame.getAppId());
		game.setName(steamGame.getName());
		game.setIconURL(steamGame.getImageLogoUrl());
		return game;
	}

}