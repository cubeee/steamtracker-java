package com.x7ff.steam.domain.repository.statistics;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.x7ff.steam.domain.MostPlayedGame;
import com.x7ff.steam.domain.Player;
import com.x7ff.steam.domain.repository.GameRepository;
import org.jooq.DSLContext;
import org.jooq.GroupField;
import org.jooq.Param;
import org.jooq.Record4;
import org.jooq.Table;
import org.springframework.stereotype.Component;

import static org.jooq.impl.DSL.param;

@Component("playerGameStatistics")
public final class PlayerGameStatistics extends MostPlayedGamesStatistics {

	private final Param<Object> playerIdParam = param("player_id");

	@Inject
	public PlayerGameStatistics(EntityManager entityManager, GameRepository gameRepository, DSLContext dslContext) {
		super(entityManager, gameRepository, dslContext);
	}

	/**
	 * Gets the most played games in the last 24 hours.
	 *
	 * @param player Player to get the statistics for
	 * @param limit Maximum number of games to return
	 * @return Most played games in the last 24 hours
	 */
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

}