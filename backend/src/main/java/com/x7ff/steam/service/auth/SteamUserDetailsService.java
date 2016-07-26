package com.x7ff.steam.service.auth;

import javax.inject.Inject;

import com.x7ff.steam.domain.repository.PlayerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SteamUserDetailsService implements UserDetailsService {

	@Inject
	private PlayerRepository playerRepository;

	@Override
	public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
		identifier = identifier.substring(identifier.lastIndexOf("/") + 1, identifier.length());
		return playerRepository.findByIdentifier(identifier);
	}

}