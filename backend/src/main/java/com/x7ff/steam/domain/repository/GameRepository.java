package com.x7ff.steam.domain.repository;

import java.util.List;

import com.x7ff.steam.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Integer> {

	default void persist(List<Game> games) {
		if (games.isEmpty()) {
			return;
		}
		save(games);
	}

}