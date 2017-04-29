package com.x7ff.steam.shared.domain.repository;

import java.util.List;

import com.x7ff.steam.shared.domain.Player;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

	@Cacheable(value = "players")
	Player findByIdentifier(String identifier);

	@CacheEvict(value = "players", allEntries = true)
	<S extends Player> List<S> save(Iterable<S> entities);

	@Cacheable(value = "players")
	long count();

}