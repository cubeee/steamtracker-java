package com.x7ff.steam.domain.repository;

import java.math.BigInteger;
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
import com.x7ff.steam.config.SteamTrackerConfig;
import com.x7ff.steam.domain.Game;
import com.x7ff.steam.domain.GameSnapshot;
import com.x7ff.steam.domain.MostPlayedGame;
import com.x7ff.steam.domain.Player;
import com.x7ff.steam.domain.converter.LocalDateTimeAttributeConverter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class StatsRepository extends SimpleJpaRepository<Player, Long> {
	public static final LocalDateTime FAR_DATE = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0);
	public static final int NO_LIMIT = -1;

	private final SteamTrackerConfig steamTrackerConfig;
	private final EntityManager entityManager;
	private final GameRepository gameRepository;

	@Inject
	public StatsRepository(SteamTrackerConfig steamTrackerConfig, EntityManager entityManager, GameRepository gameRepository) {
		super(Player.class, entityManager);
		this.steamTrackerConfig = steamTrackerConfig;
		this.entityManager = entityManager;
		this.gameRepository = gameRepository;
	}

	/**
	 * Return the total minutes ever played of each player's games.
	 *
	 * @return Collective minutes every player has played their games for.
	 */
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

	/**
	 * Returns the total minutes played by each player since they started to be tracked.
	 *
	 * @return Collective tracked minutes played of players' games.
	 */
	@Cacheable(value = "players")
	public long getCollectiveMinutesTracked() {
		long minutesPlayed = 0;
		for (MostPlayedGame mostPlayedGame : getMostPlayedGames(FAR_DATE, LocalDateTime.now(), NO_LIMIT)) {
			minutesPlayed += mostPlayedGame.getMinutesPlayed();
		}
		return minutesPlayed;
	}

	/**
	 * Gets the most played games between two given {@link LocalDateTime}s in an descending order with the specified limit.
	 *
	 * @param from Starting {@link LocalDateTime}
	 * @param to Ending {@link LocalDateTime}
	 * @param limit Number of games to return, <i>StatsRepository.NO_LIMIT</i> or -1 for no limit
	 * @return Most played games
	 */
	@Cacheable(value = "players")
	@SuppressWarnings("unchecked")
	public List<MostPlayedGame> getMostPlayedGames(LocalDateTime from, LocalDateTime to, int limit) {
		List<MostPlayedGame> games = Lists.newArrayList();

		// If you need to do that then change the lag function to coalesce(lag(minutes_played), minutes_played)
		// Also it doesn't account for a non-existent snapshot then a new snapshot
		Query query = entityManager.createNativeQuery(
				"SELECT SUM(increase), game_id " +
				"FROM (" +
				"  SELECT" +
				"    player_id," +
				"    game_id," +
				"    minutes_played - lag(minutes_played) OVER (PARTITION BY game_id, player_id ORDER BY date asc) AS increase," +
				"    date" +
				"   FROM game_snapshot" +
				") " +
				"AS snapshot_diff " +
				"WHERE snapshot_diff.increase >= 0 AND (snapshot_diff.date >= :dateFrom AND snapshot_diff.date <= :dateTo) " +
				"GROUP BY player_id, game_id " +
				"ORDER BY sum DESC");
		query.setParameter("dateFrom", LocalDateTimeAttributeConverter.convertToTimestamp(from));
		query.setParameter("dateTo", LocalDateTimeAttributeConverter.convertToTimestamp(to));
		if (limit != NO_LIMIT) {
			query = query.setMaxResults(limit);
		}

		boolean showUnplayed = steamTrackerConfig.getFrontPage().showUnplayedGames();

		List<Object[]> result = query.getResultList();
		for (Object[] val : result) {
			BigInteger played = (BigInteger) val[0];
			if (played.longValue() == 0 && !showUnplayed) {
				continue;
			}
			Integer appId = (Integer) val[1];
			Game game = gameRepository.findOne(appId); // todo: fetch from cached games

			MostPlayedGame playedGame = new MostPlayedGame(game, played.longValue());
			games.add(playedGame);
		}
		return games;
	}

	/**
	 * Gets the most played games since the beginning of tracking.
	 *
	 * @param limit Maximum number of games to return
	 * @return Most played games since the beginning of tracking
	 */
	@Cacheable(value = "players")
	public List<MostPlayedGame> getAllTimeMostPlayed(int limit) {
		return getMostPlayedGames(FAR_DATE, LocalDateTime.now(), limit);
	}

	/**
	 * Gets the most played games in the last 7 days.
	 *
	 * @param limit Maximum number of games to return
	 * @return Most played games in the last 7 days
	 */
	@Cacheable(value = "players")
	public List<MostPlayedGame> getLastWeekMostPlayed(int limit) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime lastWeek = now.minusDays(7);
		return getMostPlayedGames(lastWeek, now, limit);
	}

	/**
	 * Gets the most played games in the last 24 hours.
	 *
	 * @param limit Maximum number of games to return
	 * @return Most played games in the last 24 hours
	 */
	@Cacheable(value = "players")
	public List<MostPlayedGame> getTodaysMostPlayed(int limit) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime yesterday = now.minusHours(24);
		return getMostPlayedGames(yesterday, now, limit);
	}

}