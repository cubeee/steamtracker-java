package com.x7ff.steam.updater.batch.snapshots;

import com.google.common.collect.Lists;
import com.x7ff.steam.shared.domain.Player;
import com.x7ff.steam.shared.domain.repository.GameRepository;
import com.x7ff.steam.shared.domain.repository.PlayerRepository;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

@Component
public final class PlayerWriter extends JpaItemWriter<PlayerUpdate> {

    @Inject
    private PlayerRepository playerRepository;

    @Inject
    private GameRepository gameRepository;

    @Override
    public void write(List<? extends PlayerUpdate> updates) {
        List<Player> players = Lists.newArrayList();
        for (PlayerUpdate update : updates) {
            if (!update.isUpdated()) {
                continue;
            }
            Player player = update.getPlayer();
            players.add(player);
            gameRepository.persist(player.getGames());
        }
        playerRepository.save(players);

        // TODO: Clear caches
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

}