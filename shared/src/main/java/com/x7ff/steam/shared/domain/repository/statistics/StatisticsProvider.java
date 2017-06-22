package com.x7ff.steam.shared.domain.repository.statistics;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.x7ff.steam.shared.config.SteamTrackerConfig;

public abstract class StatisticsProvider<T> {

    protected final EntityManager entityManager;
    protected final SteamTrackerConfig steamTrackerConfig;

    @Inject
    public StatisticsProvider(EntityManager entityManager, SteamTrackerConfig steamTrackerConfig) {
        this.entityManager = entityManager;
        this.steamTrackerConfig = steamTrackerConfig;
    }

}