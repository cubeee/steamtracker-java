package com.x7ff.steam.domain.statistics;

import java.util.List;

public final class GameMostTrackedPlayers {

    private final List<GameMostTrackedPlayer> last24Hours;
    private final List<GameMostTrackedPlayer> last7Days;
    private final List<GameMostTrackedPlayer> allTime;

    public GameMostTrackedPlayers(List<GameMostTrackedPlayer> last24Hours, List<GameMostTrackedPlayer> last7Days,
                                  List<GameMostTrackedPlayer> allTime) {
        this.last24Hours = last24Hours;
        this.last7Days = last7Days;
        this.allTime = allTime;
    }

    public List<GameMostTrackedPlayer> getLast24Hours() {
        return last24Hours;
    }

    public List<GameMostTrackedPlayer> getLast7Days() {
        return last7Days;
    }

    public List<GameMostTrackedPlayer> getAllTime() {
        return allTime;
    }
}
