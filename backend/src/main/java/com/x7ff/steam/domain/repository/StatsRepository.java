package com.x7ff.steam.domain.repository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import com.google.common.collect.Lists;
import com.x7ff.steam.domain.Game;
import com.x7ff.steam.domain.GameRepository;
import com.x7ff.steam.domain.GameSnapshot;
import com.x7ff.steam.domain.MostPlayedGame;
import com.x7ff.steam.domain.Player;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class StatsRepository extends SimpleJpaRepository<Player, Long> {
	public static final LocalDateTime FAR_DATE = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0);

	private final EntityManager entityManager;
	private final GameRepository gameRepository;

	@Inject
	public StatsRepository(EntityManager entityManager, GameRepository gameRepository) {
		super(Player.class, entityManager);
		this.entityManager = entityManager;
		this.gameRepository = gameRepository;
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

	@Cacheable(value = "players")
	@SuppressWarnings("unchecked")
	public List<MostPlayedGame> getMostPlayedGames(LocalDateTime from, LocalDateTime to) {
		List<MostPlayedGame> games = Lists.newArrayList();

		// todo: feels cheap, improve?

		Query query = entityManager.createQuery(
				"SELECT DISTINCT g.appId, SUM(s.minutesPlayed) AS cumu " +
				"FROM GameSnapshot AS s LEFT JOIN Game AS g ON s.game.appId = g.appId " +
				"WHERE s.minutesPlayed > 0 AND s.date < :dateTo AND s.date > :dateFrom " +
				"GROUP BY g.appId " +
				"ORDER BY cumu DESC");
		query.setParameter("dateFrom", from);
		query.setParameter("dateTo", to);

		List<Object[]> result = query.setMaxResults(10).getResultList();
		for (Object[] val : result) {
			Integer appId = (Integer) val[0];
			Long played = (Long) val[1];
			Game game = gameRepository.findOne(appId);

			MostPlayedGame playedGame = new MostPlayedGame(game, played);
			games.add(playedGame);
		}
		return games;
	}

	public List<MostPlayedGame> getLastWeekMostPlayed() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime lastWeek = now.minusDays(7);
		return getMostPlayedGames(lastWeek, now);
	}

	public List<MostPlayedGame> getTodaysMostPlayed() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime yesterday = now.minusHours(24);
		return getMostPlayedGames(yesterday, now);
	}

}