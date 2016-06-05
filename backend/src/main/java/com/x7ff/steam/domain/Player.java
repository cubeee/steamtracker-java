package com.x7ff.steam.domain;

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

	@Column(name = "snapshots")
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@OrderBy(value = "minutes_played DESC")
	private List<GameSnapshot> snapshots;

	@Column(name = "game_count")
	private int gameCount = 0;

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

	public List<GameSnapshot> getSnapshots() {
		if (snapshots == null) {
			snapshots = Lists.newArrayList();
		}
		return snapshots;
	}

	public void setSnapshots(List<GameSnapshot> snapshots) {
		this.snapshots = snapshots;
	}

	public int getGameCount() {
		return gameCount;
	}

	public void setGameCount(int gameCount) {
		this.gameCount = gameCount;
	}
}