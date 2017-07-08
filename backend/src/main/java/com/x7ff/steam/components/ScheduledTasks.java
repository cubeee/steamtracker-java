package com.x7ff.steam.components;

import com.x7ff.steam.domain.repository.statistics.GameStatistics;
import com.x7ff.steam.domain.repository.statistics.MostPlayedGamesStatistics;
import com.x7ff.steam.domain.repository.statistics.PlayerGameStatistics;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class ScheduledTasks {
    private static final String EVERY_HOUR = "* 0 * * * *";

    private final MostPlayedGamesStatistics mostPlayedGamesStatistics;
    private final GameStatistics gameStatistics;
    private final PlayerGameStatistics playerGameStatistics;

    @Inject
    public ScheduledTasks(@Qualifier("mostPlayed") MostPlayedGamesStatistics mostPlayedGamesStatistics,
                          GameStatistics gameStatistics,
                          PlayerGameStatistics playerGameStatistics) {
        this.mostPlayedGamesStatistics = mostPlayedGamesStatistics;
        this.gameStatistics = gameStatistics;
        this.playerGameStatistics = playerGameStatistics;
    }

    @Scheduled(cron = EVERY_HOUR)
    public void refreshCaches() {
        mostPlayedGamesStatistics.refreshCache();
        gameStatistics.evictCaches();
        playerGameStatistics.evictCaches();
    }

}
