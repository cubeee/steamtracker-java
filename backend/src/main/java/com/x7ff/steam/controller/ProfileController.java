package com.x7ff.steam.controller;

import java.time.LocalDateTime;
import javax.inject.Inject;

import com.x7ff.steam.domain.Player;
import com.x7ff.steam.domain.repository.PlayerRepository;
import com.x7ff.steam.service.SteamPlayerService;
import com.x7ff.steam.util.exception.NotFoundException;
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
	private String profile(@PathVariable String identifier, Model model) throws NotFoundException {
		Player player = playerRepository.findPlayerByIdentifier(identifier);
		boolean updateNeeded = true;
		if (player == null) {
			player = getPlayer(null, identifier);
			updateNeeded = false;
		}

		if (player == null) {
			throw new NotFoundException();
		}

		if (player.getLastUpdated() != null) {
			LocalDateTime updateTime = player.getLastUpdated().plusHours(1);
			updateNeeded = LocalDateTime.now().isAfter(updateTime);
		}

		if (updateNeeded) {
			player = getPlayer(player, identifier);
			if (player == null) {
				throw new NotFoundException();
			}
		}
		model.addAttribute("player", player);
		return "profile";
	}

	private Player getPlayer(Player player, String identifier) {
		return steamPlayerService.fetchPlayer(player, true, identifier);
	}

}