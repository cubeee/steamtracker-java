--
-- Table for Player
--
CREATE TABLE player (
  id BIGSERIAL PRIMARY KEY,
  avatar CHARACTER VARYING(255),
  avatar_full CHARACTER VARYING(255),
  avatar_medium CHARACTER VARYING(255),
  country_code CHARACTER VARYING(255),
  creation_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  game_count INTEGER DEFAULT 0,
  identifier CHARACTER VARYING(255) NOT NULL,
  last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  name CHARACTER VARYING(255)
);

ALTER TABLE ONLY player
ADD CONSTRAINT uk_identifier UNIQUE (identifier);

--
-- Table for Game
--
CREATE TABLE game (
  app_id INTEGER NOT NULL,
  icon_url CHARACTER VARYING(255) NOT NULL,
  logo_url CHARACTER VARYING(255) NOT NULL,
  name CHARACTER VARYING(255) NOT NULL
);

ALTER TABLE ONLY game
ADD CONSTRAINT pk_game_id PRIMARY KEY (app_id);

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