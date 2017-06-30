package com.x7ff.steam.domain.statistics;

import com.x7ff.steam.shared.domain.Player;

public final class GameMostTrackedPlayer {

    private Player player;
    private long minutesPlayed;

    public GameMostTrackedPlayer(Player player, long minutesPlayed) {
        this.player = player;
        this.minutesPlayed = minutesPlayed;
    }

    public Player getPlayer() {
        return player;
    }

    public long getMinutesPlayed() {
        return minutesPlayed;
    }

}
