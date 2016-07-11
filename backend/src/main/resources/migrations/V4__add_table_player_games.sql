CREATE TABLE player_games (
  player_id bigint NOT NULL,
  games_app_id integer NOT NULL
);

ALTER TABLE ONLY player_games
ADD CONSTRAINT fk_game_id FOREIGN KEY (games_app_id) REFERENCES game(app_id);

ALTER TABLE ONLY player_games
ADD CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES player(id);

