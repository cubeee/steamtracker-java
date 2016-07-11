--
-- Table for player snapshots
--
CREATE TABLE player_snapshots (
  player_id BIGINT NOT NULL,
  snapshots_id BIGINT NOT NULL
);

ALTER TABLE ONLY player_snapshots
ADD CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES player(id);

ALTER TABLE ONLY player_snapshots
ADD CONSTRAINT fk_snapshot_id FOREIGN KEY (snapshots_id) REFERENCES game_snapshot(id) ON DELETE CASCADE;