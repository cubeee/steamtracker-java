package com.x7ff.steam.controller;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.inject.Inject;

import com.x7ff.steam.config.SteamTrackerConfig;
import com.x7ff.steam.domain.Player;
import com.x7ff.steam.domain.repository.PlayerRepositoryImpl;
import com.x7ff.steam.service.SteamPlayerService;
import com.x7ff.steam.util.SteamUtils;
import com.x7ff.steam.util.exception.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public final class ProfileController {

	private final SteamTrackerConfig steamTrackerConfig;

	private final PlayerRepositoryImpl playerRepository;

	private final SteamPlayerService steamPlayerService;

	@Inject
	public ProfileController(SteamTrackerConfig steamTrackerConfig, PlayerRepositoryImpl playerRepository, SteamPlayerService steamPlayerService) {
		this.steamTrackerConfig = steamTrackerConfig;
		this.playerRepository = playerRepository;
		this.steamPlayerService = steamPlayerService;
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

		if (updateNeeded && player.getLastUpdated() != null) {
			LocalDateTime updateTime = player.getLastUpdated().plusMinutes(steamTrackerConfig.getManualUpdateInterval());
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