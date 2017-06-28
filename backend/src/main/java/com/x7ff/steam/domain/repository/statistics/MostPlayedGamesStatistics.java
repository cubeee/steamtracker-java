package com.x7ff.steam.domain.repository.statistics;

import com.google.common.collect.Lists;
import com.x7ff.steam.config.BackendConfig;
import com.x7ff.steam.shared.domain.Game;
import com.x7ff.steam.shared.domain.GameSnapshot;
import com.x7ff.steam.shared.domain.MostPlayedGame;
import com.x7ff.steam.shared.domain.converter.ZonedDateTimeAttributeConverter;
import com.x7ff.steam.shared.domain.repository.GameRepository;
import com.x7ff.steam.shared.util.annotation.CacheableKey;
import com.x7ff.steam.shared.util.annotation.CacheableKeyGenerator;
import org.jooq.*;
import org.jooq.impl.SQLDataType;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.*;

@Component("mostPlayed")
public class MostPlayedGamesStatistics extends StatisticsProvider {
    public static final ZonedDateTime FAR_DATE = ZonedDateTime.of(
            LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0), ZoneId.systemDefault());
    public static final int NO_LIMIT = -1;

    private static final String CACHE_NAME = "frontpage_stats";
    private static final String CACHE_NAME_TODAY = "today";
    private static final String CACHE_NAME_WEEK = "week";
    private static final String CACHE_NAME_ALLTIME = "all_time";
    private static final String CACHE_NAME_COLLECTIVE = "collective";

    protected final CacheManager cacheManager;
    private final GameRepository gameRepository;
    private final DSLContext create;

    private final Param<Object> dateFrom = param("dateFrom");
    private final Param<Object> dateTo = param("dateTo");

    protected final Field<Object> gameId = field(GameSnapshot.GAME_ID);
    protected final Field<Object> playerId = field(GameSnapshot.PLAYER_ID);
    private final Field<Object> date = field(GameSnapshot.DATE);
    private final Field<Integer> minutesPlayed = field(GameSnapshot.MINUTES_PLAYED, SQLDataType.INTEGER);
    private final Field<Integer> increase
            = coalesce(lag(minutesPlayed)
            .over(partitionBy(gameId, playerId)
                    .orderBy(date.asc())), minutesPlayed);
    private final Field<Integer> inc = minutesPlayed.minus(increase).as("increase");

    @Inject
    public MostPlayedGamesStatistics(EntityManager entityManager,
                                     CacheManager cacheManager,
                                     BackendConfig backendConfig,
                                     GameRepository gameRepository,
                                     DSLContext dslContext) {
        super(entityManager, backendConfig);
        this.cacheManager = cacheManager;
        this.gameRepository = gameRepository;
        this.create = dslContext;
    }

    protected SelectJoinStep<Record4<Object, Object, Integer, Object>> getSnapshotSelect() {
        return create.select(playerId, gameId, inc, date).from(GameSnapshot.GAME_SNAPSHOT);
    }

    protected Table<Record4<Object, Object, Integer, Object>> getSnapshotDiff() {
        return getSnapshotSelect().asTable("snapshot_diff");
    }

    /**
     * Specify the fields to GROUP BY when selecting snapshots.
     *
     * @return Fields to GROUP BY
     */
    protected GroupField[] getGroupFields() {
        return new GroupField[]{gameId};
    }

    /**
     * Specify the WHERE clause conditions for selecting snapshots.
     *
     * @return Conditions for WHERE clause
     */
    protected Condition[] getWhereStepConditions() {
        return new Condition[]{
                inc.greaterThan(zero()).and(date.greaterOrEqual(dateFrom).and(date.lessOrEqual(dateTo)))
        };
    }

    /**
     * Optional step to configure and prepare the query before executing it.
     *
     * @param query   Query to be configured and prepared
     * @param context Optional context for the query
     */
    protected void prepareQuery(Query query, Optional<StatisticsContext> context) {

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
     * @param from    Starting {@link LocalDateTime}
     * @param to      Ending {@link LocalDateTime}
     * @param limit   Number of games to return, <i>StatsRepository.NO_LIMIT</i> or -1 for no limit
     * @param context Optional context for the query
     * @return Most played games
     */
    public List<MostPlayedGame> getMostPlayedGames(ZonedDateTime from, ZonedDateTime to, int limit,
                                                   Optional<StatisticsContext> context) {
        List<MostPlayedGame> games = Lists.newArrayList();

        Param<Object> dateFrom = param("dateFrom");
        Param<Object> dateTo = param("dateTo");

        Table<Record4<Object, Object, Integer, Object>> snapshotDiff = getSnapshotDiff();

        AggregateFunction<BigDecimal> sum = sum(inc);
        Field<BigDecimal> sumField = sum.as("s");

        String sql = create.renderNamedParams(
                select(sumField, gameId)
                        .from(snapshotDiff)
                        .where(getWhereStepConditions())
                        .groupBy(getGroupFields())
                        .orderBy(sumField.desc()));
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(dateFrom.getParamName(), ZonedDateTimeAttributeConverter.convertToTimestamp(from));
        query.setParameter(dateTo.getParamName(), ZonedDateTimeAttributeConverter.convertToTimestamp(to));
        prepareQuery(query, context);
        if (limit != NO_LIMIT) {
            query = query.setMaxResults(limit);
        }
        @SuppressWarnings("unchecked")
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
     * Returns the total minutes played by each player since they started to be tracked.
     *
     * @param context Optional context for the query
     * @return Collective tracked minutes played of players' games.
     */
    @Cacheable(value = CACHE_NAME, keyGenerator = CacheableKeyGenerator.NAME)
    @CacheableKey(CACHE_NAME_COLLECTIVE)
    public long getCollectiveMinutesTracked(Optional<StatisticsContext> context) {
        long minutesPlayed = 0;
        for (MostPlayedGame mostPlayedGame : getMostPlayedGames(FAR_DATE, ZonedDateTime.now(), NO_LIMIT, context)) {
            minutesPlayed += mostPlayedGame.getMinutesPlayed();
        }
        return minutesPlayed;
    }

    /**
     * Gets the most played games in the last 24 hours.
     *
     * @param limit   Maximum number of games to return
     * @param context Optional context for the query
     * @return Most played games in the last 24 hours
     */
    @Cacheable(value = CACHE_NAME, keyGenerator = CacheableKeyGenerator.NAME)
    @CacheableKey(CACHE_NAME_TODAY)
    public List<MostPlayedGame> getTodaysMostPlayed(int limit, Optional<StatisticsContext> context) {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime yesterday = now.minusHours(24);
        return getMostPlayedGames(yesterday, now, limit, context);
    }

    /**
     * Gets the most played games in the last 7 days.
     *
     * @param limit   Maximum number of games to return
     * @param context Optional context for the query
     * @return Most played games in the last 7 days
     */
    @Cacheable(value = CACHE_NAME, keyGenerator = CacheableKeyGenerator.NAME)
    @CacheableKey(CACHE_NAME_WEEK)
    public List<MostPlayedGame> getLastWeekMostPlayed(int limit, Optional<StatisticsContext> context) {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime lastWeek = now.minusDays(7);
        return getMostPlayedGames(lastWeek, now, limit, context);
    }

    /**
     * Gets the most played games since the beginning of tracking.
     *
     * @param limit   Maximum number of games to return
     * @param context Optional context for the query
     * @return Most played games since the beginning of tracking
     */
    @Cacheable(value = CACHE_NAME, keyGenerator = CacheableKeyGenerator.NAME)
    @CacheableKey(CACHE_NAME_ALLTIME)
    public List<MostPlayedGame> getAllTimeMostPlayed(int limit, Optional<StatisticsContext> context) {
        return getMostPlayedGames(FAR_DATE, ZonedDateTime.now(), limit, context);
    }

    /**
     * Call the cacheable methods to cache them
     */
    public void refreshCache() {
        Optional<StatisticsContext> context = Optional.empty();
        int limit = backendConfig.getFrontPage().getGamesInTables();

        String[] refreshedCacheNames = {
                CACHE_NAME_COLLECTIVE,
                CACHE_NAME_TODAY,
                CACHE_NAME_WEEK,
                CACHE_NAME_ALLTIME,
        };

        Cache cache = cacheManager.getCache(CACHE_NAME);
        for (String cacheName : refreshedCacheNames) {
            cache.evict(cacheName);
            switch (cacheName) {
                case CACHE_NAME_COLLECTIVE:
                    cache.put(CACHE_NAME_COLLECTIVE, getCollectiveMinutesTracked(context));
                    break;
                case CACHE_NAME_TODAY:
                    cache.put(CACHE_NAME_TODAY, getTodaysMostPlayed(limit, context));
                    break;
                case CACHE_NAME_WEEK:
                    cache.put(CACHE_NAME_WEEK, getLastWeekMostPlayed(limit, context));
                    break;
                case CACHE_NAME_ALLTIME:
                    cache.put(CACHE_NAME_ALLTIME, getAllTimeMostPlayed(limit, context));
                    break;
            }
        }
    }

}