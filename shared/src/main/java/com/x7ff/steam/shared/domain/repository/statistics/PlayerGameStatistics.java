package com.x7ff.steam.shared.domain.repository.statistics;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.x7ff.steam.shared.config.SteamTrackerConfig;
import com.x7ff.steam.shared.domain.MostPlayedGame;
import com.x7ff.steam.shared.domain.Player;
import com.x7ff.steam.shared.domain.repository.GameRepository;
import org.jooq.DSLContext;
import org.jooq.GroupField;
import org.jooq.Param;
import org.jooq.Record4;
import org.jooq.Table;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import static org.jooq.impl.DSL.param;

@Component("playerGameStatistics")
public class PlayerGameStatistics extends MostPlayedGamesStatistics {
	public static final String CACHE_KEY = "playerGameStats";
	public static final String CACHE_KEY_TODAY = CACHE_KEY + "Today";
	public static final String CACHE_KEY_WEEK = CACHE_KEY + "Week";
	public static final String CACHE_KEY_ALLTIME = CACHE_KEY + "AllTime";

	private final Param<Object> playerIdParam = param("player_id");

	@Inject
	public PlayerGameStatistics(EntityManager entityManager,
	                            CacheManager cacheManager,
	                            SteamTrackerConfig steamTrackerConfig,
	                            GameRepository gameRepository,
	                            DSLContext dslContext) {
		super(entityManager, cacheManager, steamTrackerConfig, gameRepository, dslContext);
	}

	/**
	 * Gets the most played games in the last 24 hours.
	 *
	 * @param player Player to get the statistics for
	 * @param limit Maximum number of games to return
	 * @return Most played games in the last 24 hours
	 */
	@Cacheable(value = CACHE_KEY_TODAY, key = "#player?.id")
	public List<MostPlayedGame> getTodaysMostPlayed(Player player, int limit) {
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime yesterday = now.minusHours(24);
		return getMostPlayedGames(player, yesterday, now, limit);
	}

	/**
	 * Gets the most played games in the last 7 days.
	 *
	 * @param player Player to get the statistics for
	 * @param limit Maximum number of games to return
	 * @return Most played games in the last 7 days
	 */
	@Cacheable(value = CACHE_KEY_WEEK, key = "#player?.id")
	public List<MostPlayedGame> getLastWeekMostPlayed(Player player, int limit) {
		return super.getLastWeekMostPlayed(limit, getStatisticsContext(player));
	}

	/**
	 * Gets the most played games since the beginning of tracking.
	 *
	 * @param player Player to get the statistics for
	 * @param limit Maximum number of games to return
	 * @return Most played games since the beginning of tracking
	 */
	@Cacheable(value = CACHE_KEY_ALLTIME, key = "#player?.id")
	public List<MostPlayedGame> getAllTimeMostPlayed(Player player, int limit) {
		return getMostPlayedGames(player, FAR_DATE, ZonedDateTime.now(), limit);
	}

	private List<MostPlayedGame> getMostPlayedGames(Player player, ZonedDateTime from, ZonedDateTime to, int limit) {
		checkPlayerProvided(player);
		if (!player.hasId()) {
			return Lists.newArrayList();
		}
		return super.getMostPlayedGames(from, to, limit, getStatisticsContext(player));
	}

	@Override
	protected Table<Record4<Object, Object, Integer, Object>> getSnapshotDiff() {
		return getSnapshotSelect().where(playerId.eq(playerIdParam)).asTable("snapshot_diff");
	}

	@Override
	protected GroupField[] getGroupFields() {
		return new GroupField[] { playerId, gameId };
	}

	@Override
	protected void prepareQuery(Query query, Optional<StatisticsContext> context) {
		context.ifPresent(c -> {
			long playerId = c.getItem(playerIdParam.getParamName());
			query.setParameter(playerIdParam.getParamName(), playerId);
		});
	}

	private void checkPlayerProvided(Player player) {
		Preconditions.checkNotNull(player, "No player provided");
	}

	private Optional<StatisticsContext> getStatisticsContext(Player player) {
		StatisticsContext context = new StatisticsContext();
		context.addItem(playerIdParam.getParamName(), player.getId());
		return Optional.of(context);
	}

	/**
	 * Evict the caches of a player
	 */
	public void evictPlayerCaches(Long playerId) {
		String[] refreshedCacheNames = {
				CACHE_KEY_TODAY,
				CACHE_KEY_WEEK,
				CACHE_KEY_ALLTIME,
		};

		for (String cacheName : refreshedCacheNames) {
			Cache cache = cacheManager.getCache(cacheName);
			cache.evict(playerId);
		}
	}

}