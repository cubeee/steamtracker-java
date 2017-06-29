package com.x7ff.steam.controller;

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

	@Inject
	public GameController(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	@RequestMapping("/game/{appId}/")
	private String game(@PathVariable Integer appId, Model model) throws NotFoundException {
		Game game = gameRepository.findOne(appId);
		if (game == null) {
			throw new NotFoundException();
		}

		model.addAttribute("game", game);
		return "game";
	}

}