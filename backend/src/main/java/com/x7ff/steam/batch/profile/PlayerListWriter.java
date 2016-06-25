package com.x7ff.steam.batch.profile;

import java.util.List;
import javax.inject.Inject;

import com.x7ff.steam.domain.Player;
import com.x7ff.steam.domain.repository.PlayerRepository;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.stereotype.Component;

@Component
public final class PlayerListWriter extends JpaItemWriter<List<Player>> {

	@Inject
	private PlayerRepository playerRepository;

	@Override
	public void write(List<? extends List<Player>> items) {
		for (List<Player> players : items) {
			playerRepository.save(players);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

}