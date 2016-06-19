package com.x7ff.steam.domain.repository;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.common.collect.Lists;
import com.x7ff.steam.domain.Game;
import com.x7ff.steam.domain.MostPlayedGame;
import com.x7ff.steam.domain.converter.ZonedDateTimeAttributeConverter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
public class StatsRepository {
	public static final ZonedDateTime FAR_DATE = ZonedDateTime.of(LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0), ZoneId.systemDefault());
	public static final int NO_LIMIT = -1;

	private final EntityManager entityManager;
	private final GameRepository gameRepository;

	@Inject
	public StatsRepository(EntityManager entityManager, GameRepository gameRepository) {
		this.entityManager = entityManager;
		this.gameRepository = gameRepository;
	}

	/**
	 * Returns the total minutes played by each player since they started to be tracked.
	 *
	 * @return Collective tracked minutes played of players' games.
	 */
	@Cacheable(value = "players")
	public long getCollectiveMinutesTracked() {
		long minutesPlayed = 0;
		for (MostPlayedGame mostPlayedGame : getMostPlayedGames(FAR_DATE, ZonedDateTime.now(), NO_LIMIT)) {
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
	public List<MostPlayedGame> getMostPlayedGames(ZonedDateTime from, ZonedDateTime to, int limit) {
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
		query.setParameter("dateFrom", ZonedDateTimeAttributeConverter.convertToTimestamp(from));
		query.setParameter("dateTo", ZonedDateTimeAttributeConverter.convertToTimestamp(to));
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
		return getMostPlayedGames(FAR_DATE, ZonedDateTime.now(), limit);
	}

	/**
	 * Gets the most played games in the last 7 days.
	 *
	 * @param limit Maximum number of games to return
	 * @return Most played games in the last 7 days
	 */
	@Cacheable(value = "players")
	public List<MostPlayedGame> getLastWeekMostPlayed(int limit) {
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime lastWeek = now.minusDays(7);
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
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime yesterday = now.minusHours(24);
		return getMostPlayedGames(yesterday, now, limit);
	}

}