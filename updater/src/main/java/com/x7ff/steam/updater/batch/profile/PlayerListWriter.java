package com.x7ff.steam.updater.batch.profile;

import java.util.List;
import javax.inject.Inject;

import com.x7ff.steam.shared.domain.repository.GameRepository;
import com.x7ff.steam.shared.domain.repository.PlayerRepository;
import com.x7ff.steam.shared.domain.Player;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.stereotype.Component;

@Component
public final class PlayerListWriter extends JpaItemWriter<List<Player>> {

    private boolean saveGames;

    @Inject
    private GameRepository gameRepository;

    @Inject
    private PlayerRepository playerRepository;

    @Override
    public void write(List<? extends List<Player>> items) {
        for (List<Player> players : items) {
            if (saveGames) {
                for (Player player : players) {
                    gameRepository.persist(player.getGames());
                }
            }
            playerRepository.save(players);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public void setSaveGames(boolean saveGames) {
        this.saveGames = saveGames;
    }
}