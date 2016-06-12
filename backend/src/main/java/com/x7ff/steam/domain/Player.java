package com.x7ff.steam.domain;

import java.time.LocalDateTime;
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

	@Column(name = "game_count")
	private int gameCount = 0;

	@Column(nullable = false)
	private LocalDateTime creationTime;

	@Column(nullable = false)
	private LocalDateTime lastUpdated;

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

	public int getGameCount() {
		return gameCount;
	}

	public void setGameCount(int gameCount) {
		this.gameCount = gameCount;
	}

	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
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

}