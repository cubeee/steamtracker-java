package com.x7ff.steam.controller;

import java.util.Optional;
import javax.inject.Inject;

import com.x7ff.steam.config.SteamTrackerConfig;
import com.x7ff.steam.domain.PlayerSearch;
import com.x7ff.steam.domain.repository.PlayerRepository;
import com.x7ff.steam.domain.repository.StatsRepository;
import com.x7ff.steam.util.SteamUtils;
import com.x7ff.steam.util.exception.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public final class IndexController {

	private final SteamTrackerConfig steamTrackerConfig;
	private final PlayerRepository playerRepository;
	private final StatsRepository statsRepository;

	@Inject
	public IndexController(SteamTrackerConfig steamTrackerConfig, PlayerRepository playerRepository,
	                       StatsRepository statsRepository) {
		this.steamTrackerConfig = steamTrackerConfig;
		this.playerRepository = playerRepository;
		this.statsRepository = statsRepository;
	}

	@RequestMapping("/")
	public String index(Model model) {
		int games = steamTrackerConfig.getFrontPage().getGamesInTables();
		model.addAttribute("collective_tracked", statsRepository.getCollectiveMinutesTracked());
		model.addAttribute("tracked_players", playerRepository.count());
		model.addAttribute("most_played", statsRepository.getAllTimeMostPlayed(games));
		model.addAttribute("todays_played", statsRepository.getTodaysMostPlayed(games));
		model.addAttribute("weeks_played", statsRepository.getLastWeekMostPlayed(games));
		return "index";
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String playerSearch(@ModelAttribute PlayerSearch search) throws NotFoundException {
		Optional<String> identifier = SteamUtils.resolveId(search.getIdentifier());
		identifier.orElseThrow(NotFoundException::new);
		return "redirect:/player/" + identifier.get() + "/";
	}

}