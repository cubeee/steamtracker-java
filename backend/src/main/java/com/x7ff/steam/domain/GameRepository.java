package com.x7ff.steam.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {

	default void persist(List<Game> games) {
		if (games.isEmpty()) {
			return;
		}
		save(games);
	}

}