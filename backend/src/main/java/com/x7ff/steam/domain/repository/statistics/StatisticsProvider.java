package com.x7ff.steam.domain.repository.statistics;

import com.x7ff.steam.config.BackendConfig;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public abstract class StatisticsProvider {

    protected final EntityManager entityManager;
    protected final BackendConfig backendConfig;

    @Inject
    public StatisticsProvider(EntityManager entityManager, BackendConfig backendConfig) {
        this.entityManager = entityManager;
        this.backendConfig = backendConfig;
    }

}