package com.x7ff.steam.shared.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.x7ff.steam.shared.domain.steam.SteamGame;

@Entity
public final class Game {

    @Id
    @Column(name = "app_id", unique = true)
    private Integer appId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "logo_url", nullable = false)
    private String logoUrl;

    @Column(name = "icon_url", nullable = false)
    private String iconUrl;

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

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public static Game from(SteamGame steamGame) {
        Game game = new Game();
        game.setAppId(steamGame.getAppId());
        game.setName(steamGame.getName());
        game.setIconUrl(steamGame.getImageIconUrl());
        game.setLogoUrl(steamGame.getImageLogoUrl());
        return game;
    }

}