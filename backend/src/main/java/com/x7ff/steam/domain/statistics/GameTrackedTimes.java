package com.x7ff.steam.domain.statistics;

public final class GameTrackedTimes {

    private final long last24Hours;

    private final long last7Days;

    private final long allTime;

    public GameTrackedTimes(long last24Hours, long last7Days, long allTime) {
        this.last24Hours = last24Hours;
        this.last7Days = last7Days;
        this.allTime = allTime;
    }

    public long getLast24Hours() {
        return last24Hours;
    }

    public long getLast7Days() {
        return last7Days;
    }

    public long getAllTime() {
        return allTime;
    }
}
