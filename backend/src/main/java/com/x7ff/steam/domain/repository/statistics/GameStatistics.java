package com.x7ff.steam.domain.repository.statistics;

import com.google.common.collect.Lists;
import com.x7ff.steam.config.BackendConfig;
import com.x7ff.steam.domain.statistics.GameMostTrackedPlayer;
import com.x7ff.steam.domain.statistics.GameMostTrackedPlayers;
import com.x7ff.steam.domain.statistics.GameTrackedTimes;
import com.x7ff.steam.shared.domain.Player;
import com.x7ff.steam.shared.domain.converter.ZonedDateTimeAttributeConverter;
import com.x7ff.steam.shared.domain.repository.PlayerRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Component
public class GameStatistics extends StatisticsProvider {
    public static final ZonedDateTime FAR_DATE = ZonedDateTime.of(
            LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0), ZoneId.systemDefault());
    public static final int NO_LIMIT = -1;

    private static final String CACHE_TRACKED_TIMES = "game_tracked_times";
    private static final String CACHE_MOST_TRACKED_PLAYERS = "game_most_tracked_players";

    private final PlayerRepository playerRepository;

    @Inject
    public GameStatistics(EntityManager entityManager, BackendConfig backendConfig,
                          PlayerRepository playerRepository) {
        super(entityManager, backendConfig);
        this.playerRepository = playerRepository;
    }

    @SuppressWarnings("unchecked")
    public List<GameMostTrackedPlayer> getMostTrackedPlayers(long gameId, ZonedDateTime dateFrom, ZonedDateTime dateTo,
                                                             int limit) {
        List<GameMostTrackedPlayer> mostTrackedPlayers = Lists.newArrayList();

        String sql = "SELECT * FROM game_most_tracked_players(:game_id, :dateFrom, :dateTo) " +
                "AS f(player_id BIGINT, s BIGINT)";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("game_id", gameId);
        query.setParameter("dateFrom", ZonedDateTimeAttributeConverter.convertToTimestamp(dateFrom));
        query.setParameter("dateTo", ZonedDateTimeAttributeConverter.convertToTimestamp(dateTo));
        if (limit != NO_LIMIT) {
            query.setMaxResults(limit);
        }

        List<Object[]> result = query.getResultList();
        for (Object[] rs : result) {
            BigInteger playerId = (BigInteger) rs[0];
            BigInteger played = (BigInteger) rs[1];

            Player player = playerRepository.findOne(playerId.longValue());
            mostTrackedPlayers.add(new GameMostTrackedPlayer(player, played.longValue()));
        }
        return mostTrackedPlayers;
    }

    public long getTotalTimeTracked(long gameId, ZonedDateTime dateFrom, ZonedDateTime dateTo) {
        String sql = "SELECT * FROM collective_game_minutes_tracked(:game_id, :dateFrom, :dateTo)";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("game_id", gameId);
        query.setParameter("dateFrom", ZonedDateTimeAttributeConverter.convertToTimestamp(dateFrom));
        query.setParameter("dateTo", ZonedDateTimeAttributeConverter.convertToTimestamp(dateTo));
        Object result = query.getSingleResult();
        return result == null ? 0 : ((BigInteger) result).longValue();
    }

    @Cacheable(value = CACHE_TRACKED_TIMES, key = "#gameId")
    public GameTrackedTimes getTrackedTimes(long gameId) {
        ZonedDateTime dateTo = ZonedDateTime.now();

        ZonedDateTime dateFrom = dateTo.minusHours(24);
        long last24Hours = getTotalTimeTracked(gameId, dateFrom, dateTo);

        dateFrom = dateTo.minusDays(7);
        long last7Days = getTotalTimeTracked(gameId, dateFrom, dateTo);

        long allTime = getTotalTimeTracked(gameId, FAR_DATE, dateTo);
        return new GameTrackedTimes(last24Hours, last7Days, allTime);
    }

    @Cacheable(value = CACHE_MOST_TRACKED_PLAYERS, key = "#gameId")
    public GameMostTrackedPlayers getMostTrackedPlayers(long gameId, int limit) {
        ZonedDateTime dateTo = ZonedDateTime.now();

        ZonedDateTime dateFrom = dateTo.minusHours(24);
        List<GameMostTrackedPlayer> last24Hours = getMostTrackedPlayers(gameId, dateFrom, dateTo, limit);

        dateFrom = dateTo.minusDays(7);
        List<GameMostTrackedPlayer> last7Days = getMostTrackedPlayers(gameId, dateFrom, dateTo, limit);

        List<GameMostTrackedPlayer> allTime = getMostTrackedPlayers(gameId, FAR_DATE, dateTo, limit);
        return new GameMostTrackedPlayers(last24Hours, last7Days, allTime);
    }

}
