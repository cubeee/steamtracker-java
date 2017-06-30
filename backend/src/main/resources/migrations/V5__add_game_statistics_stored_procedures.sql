CREATE OR REPLACE FUNCTION collective_game_minutes_tracked(gameId BIGINT, dateFrom TIMESTAMP, dateTo TIMESTAMP)
  RETURNS BIGINT AS $$
  DECLARE
    ret BIGINT;
  BEGIN
    WITH aggregate AS (
      SELECT
        player_id,
        game_id,
        minutes_played - lag(minutes_played) OVER
          (PARTITION BY game_id, player_id ORDER BY date ASC) AS diff,
        date
      FROM game_snapshot
      WHERE game_id = gameId
        AND date >= dateFrom
        AND date <= dateTo
    )
    SELECT SUM(diff) INTO ret
    FROM aggregate agg
    GROUP BY game_id
    HAVING SUM(diff) > 0;
    RETURN ret;
  END;
$$ LANGUAGE plpgsql;