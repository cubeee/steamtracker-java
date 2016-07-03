package com.x7ff.steam.controller;

import java.util.Optional;
import javax.inject.Inject;

import com.x7ff.steam.config.SteamTrackerConfig;
import com.x7ff.steam.domain.Player;
import com.x7ff.steam.domain.repository.PlayerRepository;
import com.x7ff.steam.domain.repository.statistics.PlayerGameStatistics;
import com.x7ff.steam.service.steam.SteamPlayerService;
import com.x7ff.steam.util.SteamUtils;
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

	private final boolean manualUpdatingEnabled;

	@Inject
	public ProfileController(SteamTrackerConfig steamTrackerConfig,
	                         PlayerRepository playerRepository,
	                         SteamPlayerService steamPlayerService,
	                         @Qualifier("playerGameStatistics") PlayerGameStatistics statsRepository) {
		this.steamTrackerConfig = steamTrackerConfig;
		this.playerRepository = playerRepository;
		this.steamPlayerService = steamPlayerService;
		this.statsRepository = statsRepository;
		this.manualUpdatingEnabled = steamTrackerConfig.getUpdater().isEnableManualUpdating();
	}

	@RequestMapping("/player/{rawIdentifier}/")
	private String profile(@PathVariable String rawIdentifier, Model model) throws NotFoundException {
		Optional<String> optionalIdentifier = SteamUtils.resolveId(rawIdentifier);
		optionalIdentifier.orElseThrow(NotFoundException::new);
		String identifier = optionalIdentifier.get();

		Player player = playerRepository.findByIdentifier(identifier);
		boolean updateNeeded = true;
		if (player == null) {
			player = getPlayer(null, identifier);
			updateNeeded = false;
		}

		if (player == null) {
			throw new NotFoundException();
		}

		if (manualUpdatingEnabled) {
			if (updateNeeded) {
				updateNeeded = player.updateNeeded(steamTrackerConfig.getUpdater().getManualUpdateInterval());
			}
			if (updateNeeded) {
				player = getPlayer(player, identifier);
				if (player == null) {
					throw new NotFoundException();
				}
			}
		}

		int games = steamTrackerConfig.getFrontPage().getGamesInTables();
		model.addAttribute("most_played", statsRepository.getAllTimeMostPlayed(player, games));
		model.addAttribute("todays_played", statsRepository.getTodaysMostPlayed(player, games));
		model.addAttribute("weeks_played", statsRepository.getLastWeekMostPlayed(player, games));
		model.addAttribute("player", player);
		return "profile";
	}

	private Player getPlayer(Player player, String identifier) {
		return steamPlayerService.fetchPlayer(player, true, identifier);
	}

}