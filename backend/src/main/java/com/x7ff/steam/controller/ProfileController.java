package com.x7ff.steam.controller;

import java.util.Optional;
import javax.inject.Inject;

import com.x7ff.steam.shared.config.SteamTrackerConfig;
import com.x7ff.steam.shared.domain.Player;
import com.x7ff.steam.shared.domain.repository.PlayerRepository;
import com.x7ff.steam.shared.domain.repository.statistics.PlayerGameStatistics;
import com.x7ff.steam.shared.service.steam.FetchOption;
import com.x7ff.steam.shared.service.steam.SteamPlayerService;
import com.x7ff.steam.shared.util.SteamUtils;
import com.x7ff.steam.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public final class ProfileController {

    private final SteamTrackerConfig steamTrackerConfig;
    private final PlayerRepository playerRepository;
    private final SteamPlayerService steamPlayerService;
    private final PlayerGameStatistics statsRepository;

    @Inject
    public ProfileController(SteamTrackerConfig steamTrackerConfig,
                             PlayerRepository playerRepository,
                             SteamPlayerService steamPlayerService,
                             @Qualifier("playerGameStatistics") PlayerGameStatistics statsRepository) {
        this.steamTrackerConfig = steamTrackerConfig;
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

        int games = steamTrackerConfig.getFrontPage().getGamesInTables();
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