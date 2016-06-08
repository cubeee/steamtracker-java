package com.x7ff.steam.controller;

import java.util.Optional;
import javax.inject.Inject;

import com.x7ff.steam.domain.Player;
import com.x7ff.steam.domain.repository.PlayerRepository;
import com.x7ff.steam.service.SteamPlayerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public final class ProfileController {

	private final PlayerRepository playerRepository;

	private final SteamPlayerService steamPlayerService;

	@Inject
	public ProfileController(PlayerRepository playerRepository, SteamPlayerService steamPlayerService) {
		this.playerRepository = playerRepository;
		this.steamPlayerService = steamPlayerService;
	}

	@RequestMapping("/player/{identifier}/")
	private String profile(@PathVariable String identifier, Model model) {
		Player player = playerRepository.findPlayerByIdentifier(identifier);
		if (player == null) {
			Optional<Player> steamPlayer = steamPlayerService.fetchPlayer(identifier);
			if (steamPlayer.isPresent()) {
				player = steamPlayer.get();
			}
		}
		model.addAttribute("player", player);
		return "profile";
	}

}