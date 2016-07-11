--
-- Table for GameSnapshot
--
CREATE TABLE game_snapshot (
  id BIGSERIAL PRIMARY KEY ,
  date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  minutes_played INTEGER NOT NULL,
  game_id INTEGER NOT NULL,
  player_id BIGINT
);

ALTER TABLE ONLY game_snapshot
ADD CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES player(id);

ALTER TABLE ONLY game_snapshot
ADD CONSTRAINT fk_game_id FOREIGN KEY (game_id) REFERENCES game(app_id);
