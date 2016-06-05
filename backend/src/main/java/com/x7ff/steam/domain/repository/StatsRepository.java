package com.x7ff.steam.domain.repository;

import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import com.google.common.collect.Lists;
import com.x7ff.steam.domain.GameSnapshot;
import com.x7ff.steam.domain.MostPlayedGame;
import com.x7ff.steam.domain.Player;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class StatsRepository extends SimpleJpaRepository<Player, Long> {

	private final EntityManager entityManager;

	@Inject
	public StatsRepository(EntityManager entityManager) {
		super(Player.class, entityManager);
		this.entityManager = entityManager;
	}

	@Cacheable(value = "players")
	public long getCollectiveMinutesPlayed() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Number> criteria = criteriaBuilder.createQuery(Number.class);
		Root<GameSnapshot> root = criteria.from(GameSnapshot.class);
		Expression<Number> sum = criteriaBuilder.sum(root.get("minutesPlayed"));
		criteria.select(sum.alias("s"));

		Number minutes = entityManager.createQuery(criteria).getSingleResult();
		return minutes == null ? 0 : minutes.longValue();
	}

	@CacheEvict(value = "players")
	public List<MostPlayedGame> getMostPlayedGames() {
		List<MostPlayedGame> games = Lists.newArrayList();

		Query query = entityManager.createQuery(
				"SELECT DISTINCT g.name, g.logoUrl, SUM(s.minutesPlayed) AS cumu " +
				"FROM com.x7ff.steam.domain.GameSnapshot AS s LEFT JOIN com.x7ff.steam.domain.Game AS g ON s.game.appId = g.appId " +
				"WHERE s.minutesPlayed > 0 " +
				"GROUP BY g.name, g.logoUrl " +
				"ORDER BY cumu DESC");
		List<Object[]> result = query.setMaxResults(10).getResultList();
		for (Object[] val : result) {
			MostPlayedGame game = new MostPlayedGame((String) val[0], (String) val[1], (Long) val[2]);
			games.add(game);
		}
		return games;
	}

}