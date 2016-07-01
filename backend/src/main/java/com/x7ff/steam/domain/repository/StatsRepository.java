package com.x7ff.steam.domain.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.common.collect.Lists;
import com.x7ff.steam.domain.Game;
import com.x7ff.steam.domain.GameSnapshot;
import com.x7ff.steam.domain.MostPlayedGame;
import com.x7ff.steam.domain.Player;
import com.x7ff.steam.domain.converter.ZonedDateTimeAttributeConverter;
import org.jooq.AggregateFunction;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.GroupField;
import org.jooq.Param;
import org.jooq.Record4;
import org.jooq.SelectJoinStep;
import org.jooq.Table;
import org.jooq.impl.SQLDataType;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import static org.jooq.impl.DSL.*;

@Repository
public class StatsRepository {
	public static final ZonedDateTime FAR_DATE = ZonedDateTime.of(LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0), ZoneId.systemDefault());
	public static final int NO_LIMIT = -1;

	private final EntityManager entityManager;
	private final GameRepository gameRepository;

	private final DSLContext create;

	@Inject
	public StatsRepository(EntityManager entityManager, GameRepository gameRepository, DSLContext dslContext) {
		this.entityManager = entityManager;
		this.gameRepository = gameRepository;
		this.create = dslContext;
	}

	/**
	 * Returns the total minutes played by each player since they started to be tracked.
	 *
	 * @return Collective tracked minutes played of players' games.
	 */
	@Cacheable(value = "players")
	public long getCollectiveMinutesTracked() {
		return getCollectiveMinutesTracked(Optional.empty());
	}

	/**
	 * Returns the total minutes played by each player since they started to be tracked.
	 *
	 * @return Collective tracked minutes played of players' games.
	 */
	@Cacheable(value = "players")
	public long getCollectiveMinutesTracked(Optional<Player> player) {
		long minutesPlayed = 0;
		for (MostPlayedGame mostPlayedGame : getMostPlayedGames(player, FAR_DATE, ZonedDateTime.now(), NO_LIMIT)) {
			minutesPlayed += mostPlayedGame.getMinutesPlayed();
		}
		return minutesPlayed;
	}

	/**
	 * Gets the most played games between two given {@link LocalDateTime}s in an descending order with the specified limit.
	 * <p>
	 * jOOQ query results to a similar looking query:
	 * <code><pre>
	 * SELECT SUM(increase) s, game_id
	 * FROM (
	 *    SELECT
	 *    player_id,
	 *    game_id,
	 *    minutes_played - lag(minutes_played) OVER (PARTITION BY game_id, player_id ORDER BY date asc) AS increase,
	 *    date
	 * FROM game_snapshot
	 * AS snapshot_diff
	 * WHERE snapshot_diff.increase >= 0 AND (snapshot_diff.date >= :dateFrom AND snapshot_diff.date <= :dateTo)
	 * GROUP BY game_id
	 * ORDER BY s DESC
	 * </pre></code>
	 *
	 * @param from Starting {@link LocalDateTime}
	 * @param to Ending {@link LocalDateTime}
	 * @param limit Number of games to return, <i>StatsRepository.NO_LIMIT</i> or -1 for no limit
	 * @return Most played games
	 */
	@Cacheable(value = "players")
	@SuppressWarnings("unchecked")
	public List<MostPlayedGame> getMostPlayedGames(Optional<Player> player, ZonedDateTime from, ZonedDateTime to, int limit) {
		List<MostPlayedGame> games = Lists.newArrayList();
		Player p = player.orElse(null);
		if (player.isPresent() && !p.hasId()) {
			return games;
		}

		Param<Object> dateFrom = param("dateFrom");
		Param<Object> dateTo = param("dateTo");
		Param<Object> playerIdParam = param("player_id");

		Field<Object> gameId = field(GameSnapshot.GAME_ID);
		Field<Object> playerId = field(GameSnapshot.PLAYER_ID);
		Field<Object> date = field(GameSnapshot.DATE);
		Field<Integer> minutesPlayed = field(GameSnapshot.MINUTES_PLAYED, SQLDataType.INTEGER);
		Field<Integer> increase = coalesce(lag(minutesPlayed).over(partitionBy(gameId, playerId).orderBy(date.asc())), minutesPlayed);
		Field<Integer> inc = minutesPlayed.minus(increase).as("increase");

		SelectJoinStep<Record4<Object, Object, Integer, Object>> snapshotSelect = create.select(
				playerId,
				gameId,
				inc,
				date)
				.from(GameSnapshot.GAME_SNAPSHOT);

		Table<Record4<Object, Object, Integer, Object>> snapshotDiff;
		if (player.isPresent()) {
			 snapshotDiff = snapshotSelect.where(playerId.eq(playerIdParam)).asTable("snapshot_diff");
		} else {
			snapshotDiff = snapshotSelect.asTable("snapshot_diff");
		}

		AggregateFunction<BigDecimal> sum = sum(inc);
		Field<BigDecimal> sumField = sum.as("s");

		GroupField[] groupFields = player.isPresent() ? new GroupField[] { gameId, playerId } : new GroupField[] { gameId };
		String sql = create.renderNamedParams(
				select(sumField, gameId)
				.from(snapshotDiff)
				.where(inc.greaterThan(zero()).and(date.greaterOrEqual(dateFrom).and(date.lessOrEqual(dateTo))))
				.groupBy(groupFields)
				.orderBy(sumField.desc()));

		Query query = entityManager.createNativeQuery(sql);
		query.setParameter(dateFrom.getParamName(), ZonedDateTimeAttributeConverter.convertToTimestamp(from));
		query.setParameter(dateTo.getParamName(), ZonedDateTimeAttributeConverter.convertToTimestamp(to));
		if (player.isPresent()) {
			query.setParameter(playerIdParam.getParamName(), p.getId());
		}
		if (limit != NO_LIMIT) {
			query = query.setMaxResults(limit);
		}

		List<Object[]> result = query.getResultList();
		for (Object[] val : result) {
			BigInteger played = (BigInteger) val[0];
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
		return getAllTimeMostPlayed(Optional.empty(), limit);
	}

	/**
	 * Gets the most played games since the beginning of tracking for an optional player.
	 *
	 * @param player Optional player to get the games for
	 * @param limit Maximum number of games to return
	 * @return Most played games since the beginning of tracking
	 */
	@Cacheable(value = "players")
	public List<MostPlayedGame> getAllTimeMostPlayed(Optional<Player> player, int limit) {
		return getMostPlayedGames(player, FAR_DATE, ZonedDateTime.now(), limit);
	}

	/**
	 * Gets the most played games in the last 7 days.
	 *
	 * @param limit Maximum number of games to return
	 * @return Most played games in the last 7 days
	 */
	@Cacheable(value = "players")
	public List<MostPlayedGame> getLastWeekMostPlayed(int limit) {
		return getLastWeekMostPlayed(Optional.empty(), limit);
	}

	/**
	 * Gets the most played games in the last 7 days for an optional player.
	 *
	 * @param player Optional player to get the games for
	 * @param limit Maximum number of games to return
	 * @return Most played games in the last 7 days
	 */
	@Cacheable(value = "players")
	public List<MostPlayedGame> getLastWeekMostPlayed(Optional<Player> player, int limit) {
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime lastWeek = now.minusDays(7);
		return getMostPlayedGames(player, lastWeek, now, limit);
	}

	/**
	 * Gets the most played games in the last 24 hours.
	 *
	 * @param limit Maximum number of games to return
	 * @return Most played games in the last 24 hours
	 */
	@Cacheable(value = "players")
	public List<MostPlayedGame> getTodaysMostPlayed(int limit) {
		return getTodaysMostPlayed(Optional.empty(), limit);
	}

	/**
	 * Gets the most played games in the last 24 hours for an optional player.
	 *
	 * @param player Optional player to get the games for
	 * @param limit Maximum number of games to return
	 * @return Most played games in the last 24 hours
	 */
	@Cacheable(value = "players")
	public List<MostPlayedGame> getTodaysMostPlayed(Optional<Player> player, int limit) {
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime yesterday = now.minusHours(24);
		return getMostPlayedGames(player, yesterday, now, limit);
	}

}