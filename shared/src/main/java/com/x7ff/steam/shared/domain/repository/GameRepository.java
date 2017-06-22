package com.x7ff.steam.shared.domain.repository;

import java.util.List;

import com.x7ff.steam.shared.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {

    default List<Game> persist(List<Game> games) {
        if (games.isEmpty()) {
            return games;
        }
        return save(games);
    }

}