package com.x7ff.steam.controller;

import com.x7ff.steam.config.BackendConfig;
import com.x7ff.steam.domain.repository.statistics.PlayerGameStatistics;
import com.x7ff.steam.shared.domain.Player;
import com.x7ff.steam.shared.domain.repository.PlayerRepository;
import com.x7ff.steam.shared.service.steam.FetchOption;
import com.x7ff.steam.shared.service.steam.SteamPlayerService;
import com.x7ff.steam.shared.util.SteamUtils;
import com.x7ff.steam.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import java.util.Optional;

@Controller
public final class ProfileController {

    private final BackendConfig backendConfig;
    private final PlayerRepository playerRepository;
    private final SteamPlayerService steamPlayerService;
    private final PlayerGameStatistics statsRepository;

    @Inject
    public ProfileController(BackendConfig backendConfig,
                             PlayerRepository playerRepository,
                             SteamPlayerService steamPlayerService,
                             @Qualifier("playerGameStatistics") PlayerGameStatistics statsRepository) {
        this.backendConfig = backendConfig;
        this.playerRepository = playerRepository;
        this.steamPlayerService = steamPlayerService;
        this.statsRepository = statsRepository;
    }

    @RequestMapping("/player/{rawIdentifier}/")
    private String profile(@PathVariable String rawIdentifier, Model model) throws NotFoundException {
        Optional<String> optionalIdentifier = SteamUtils.resolveId(rawIdentifier);
        optionalIdentifier.orElseThrow(NotFoundException::new);
        String identifier = optionalIdentifier.get();

        Player player = playerRepository.findByIdentifier(identifier);
        if (player == null) {
            player = getPlayer(null, identifier);
        }

        if (player == null) {
            throw new NotFoundException();
        }

        int games = backendConfig.getFrontPage().getGamesInTables();
        model.addAttribute("most_played", statsRepository.getAllTimeMostPlayed(player, games));
        model.addAttribute("todays_played", statsRepository.getTodaysMostPlayed(player, games));
        model.addAttribute("weeks_played", statsRepository.getLastWeekMostPlayed(player, games));
        model.addAttribute("player", player);
        return "profile";
    }

    private Player getPlayer(Player player, String identifier) {
        return steamPlayerService.fetchPlayer(player, identifier, FetchOption.SAVE_PLAYER, FetchOption.RESOLVE_PROFILE);
    }

}