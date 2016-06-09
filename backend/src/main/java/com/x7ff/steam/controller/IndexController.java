package com.x7ff.steam.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import com.x7ff.steam.domain.PlayerSearch;
import com.x7ff.steam.domain.repository.PlayerRepositoryImpl;
import com.x7ff.steam.domain.repository.StatsRepository;
import com.x7ff.steam.util.SteamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public final class IndexController {

	private final PlayerRepositoryImpl playerRepository;
	private final StatsRepository statsRepository;

	@Autowired
	public IndexController(StatsRepository statsRepository, PlayerRepositoryImpl playerRepository) {
		this.statsRepository = statsRepository;
		this.playerRepository = playerRepository;
	}

	@RequestMapping("/")
	public String index(Model model) {
		model.addAttribute("collective_played", statsRepository.getCollectiveMinutesPlayed()); // todo: calculated and cached
		model.addAttribute("tracked_players", playerRepository.count()); // todo: cached
		model.addAttribute("most_played", statsRepository.getMostPlayedGames(StatsRepository.FAR_DATE, LocalDateTime.now()));
		model.addAttribute("todays_played", statsRepository.getTodaysMostPlayed());
		model.addAttribute("weeks_played", statsRepository.getLastWeekMostPlayed());
		return "index";
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String playerSearch(@ModelAttribute PlayerSearch search) {
		Optional<String> identifier = SteamUtils.resolveId(search.getIdentifier());

		// todo: 404 if identifier is empty

		return "redirect:/player/" + identifier.get() + "/";
	}

}