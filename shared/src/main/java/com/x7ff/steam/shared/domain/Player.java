package com.x7ff.steam.shared.domain;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;

import com.google.common.collect.Lists;
import com.x7ff.steam.shared.util.CountryCode;

@Entity
public final class Player extends PlayerUserDetails {

    @Id
    @SequenceGenerator(name = "player_id_seq", sequenceName = "player_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "player_id_seq")
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(nullable = false, name = "identifier", unique = true)
    private String identifier;

    @Column(name = "name")
    private String name;

    @Column(nullable = false, name = "creation_time")
    private ZonedDateTime creationTime;

    @Column(nullable = false, name = "last_updated")
    private ZonedDateTime lastUpdated;

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

    @Column(name = "games")
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Game> games;

    @Column(name = "snapshots")
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy(value = "minutes_played DESC")
    private List<GameSnapshot> snapshots;

    protected Player() {
    }

    public Player(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getUsername() {
        return getDisplayName();
    }

    public boolean hasId() {
        return id != null;
    }

    public String getDisplayName() {
        if (name != null) {
            return name;
        } else if (identifier != null) {
            return identifier;
        }
        return "N/A";
    }

    public boolean updateNeeded(long updateInterval) {
        ZonedDateTime updateTime = lastUpdated.plusMinutes(updateInterval);
        return ZonedDateTime.now().isAfter(updateTime);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public String getCountry() {
        if (countryCode == null) {
            return null;
        }
        return CountryCode.getCountry(countryCode);
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

    public List<GameSnapshot> getSnapshots() {
        if (snapshots == null) {
            snapshots = Lists.newArrayList();
        }
        return snapshots;
    }

    public void setSnapshots(List<GameSnapshot> snapshots) {
        this.snapshots = snapshots;
    }

}