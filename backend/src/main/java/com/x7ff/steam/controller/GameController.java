package com.x7ff.steam.controller;

import com.x7ff.steam.domain.repository.statistics.GameStatistics;
import com.x7ff.steam.shared.domain.Game;
import com.x7ff.steam.shared.domain.repository.GameRepository;
import com.x7ff.steam.util.exception.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

@Controller
public final class GameController {

	private final GameRepository gameRepository;
	private final GameStatistics gameStatistics;

	@Inject
	public GameController(GameRepository gameRepository, GameStatistics gameStatistics) {
		this.gameRepository = gameRepository;
		this.gameStatistics = gameStatistics;
	}

	@RequestMapping("/game/{appId}/")
	private String game(@PathVariable Integer appId, Model model) throws NotFoundException {
		Game game = gameRepository.findOne(appId);
		if (game == null) {
			throw new NotFoundException();
		}

		model.addAttribute("game", game);
		model.addAttribute("tracked_times",
                gameStatistics.getTrackedTimes(game.getAppId()));
		model.addAttribute("tracked_players",
                gameStatistics.getMostTrackedPlayers(game.getAppId(), 10));
		return "game";
	}

}