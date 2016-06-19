package com.x7ff.steam.domain;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;

import com.google.common.collect.Lists;
import com.x7ff.steam.util.CountryCode;

@Entity
public final class Player {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, name = "identifier", unique = true)
	private String identifier;

	@Column(name = "name")
	private String name;

	@Column(name = "avatar")
	private String avatar;

	@Column(name = "avatar_medium")
	private String avatarMedium;

	@Column(name = "avatar_full")
	private String avatarFull;

	@Column(name = "country_code")
	private String countryCode;

	@Column(name = "game_count")
	private int gameCount = 0;

	@Column(nullable = false, name = "creation_time")
	private ZonedDateTime creationTime;

	@Column(nullable = false, name = "last_updated")
	private ZonedDateTime lastUpdated;

	@Column(name = "snapshots")
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy(value = "minutes_played DESC")
	private List<GameSnapshot> snapshots;

	private final transient List<Game> games = Lists.newArrayList();

	protected Player() {
	}

	public Player(String identifier) {
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAvatarMedium() {
		return avatarMedium;
	}

	public void setAvatarMedium(String avatarMedium) {
		this.avatarMedium = avatarMedium;
	}

	public String getAvatarFull() {
		return avatarFull;
	}

	public void setAvatarFull(String avatarFull) {
		this.avatarFull = avatarFull;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public int getGameCount() {
		return gameCount;
	}

	public void setGameCount(int gameCount) {
		this.gameCount = gameCount;
	}

	public ZonedDateTime getCreationTime() {
		return creationTime;
	}

	public String getFormattedCreationTime(String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return creationTime.format(formatter);
	}

	public void setCreationTime(ZonedDateTime creationTime) {
		this.creationTime = creationTime;
	}

	public ZonedDateTime getLastUpdated() {
		return lastUpdated;
	}

	public String getFormattedLastUpdate(String format) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return lastUpdated.format(formatter);
	}

	public void setLastUpdated(ZonedDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public List<GameSnapshot> getSnapshots() {
		if (snapshots == null) {
			snapshots = Lists.newArrayList();
		}
		return snapshots;
	}

	public void setSnapshots(List<GameSnapshot> snapshots) {
		this.snapshots = snapshots;
	}

	public List<Game> getGames() {
		return games;
	}

	public String getDisplayName() {
		if (name != null) {
			return name;
		} else if (identifier != null) {
			return identifier;
		}
		return "N/A";
	}

	public String getCountry() {
		if (countryCode == null) {
			return null;
		}
		return CountryCode.getCountry(countryCode);
	}

}