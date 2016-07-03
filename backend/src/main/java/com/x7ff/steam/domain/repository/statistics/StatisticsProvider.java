package com.x7ff.steam.domain.repository.statistics;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public abstract class StatisticsProvider<T> {

	protected final EntityManager entityManager;

	@Inject
	public StatisticsProvider(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}