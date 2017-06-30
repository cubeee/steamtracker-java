DROP FUNCTION IF EXISTS game_most_tracked_players(BIGINT, TIMESTAMP, TIMESTAMP);

CREATE OR REPLACE FUNCTION game_most_tracked_players(gameId BIGINT, dateFrom TIMESTAMP, dateTo TIMESTAMP)
  RETURNS SETOF RECORD
AS $$
DECLARE
  rec RECORD;
BEGIN
  FOR rec IN (
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
    SELECT
      player_id,
      SUM(diff) AS sum
    FROM aggregate agg
    GROUP by player_id
    HAVING SUM(diff) > 0
    ORDER BY sum DESC) LOOP
    RETURN NEXT rec;
  END LOOP;
  RETURN;
END;
$$ LANGUAGE plpgsql;