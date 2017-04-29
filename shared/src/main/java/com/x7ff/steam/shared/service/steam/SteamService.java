package com.x7ff.steam.shared.service.steam;

import java.util.List;
import javax.inject.Inject;

import com.google.common.base.Preconditions;
import com.x7ff.steam.shared.config.SteamTrackerConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public abstract class SteamService<R> {

	protected final SteamTrackerConfig steamTrackerConfig;

	protected final RestTemplate restTemplate;

	@Inject
	public SteamService(SteamTrackerConfig steamTrackerConfig) {
		this.steamTrackerConfig = steamTrackerConfig;
		this.restTemplate = new RestTemplate();
	}

	public abstract R fetch(String... args);

	protected abstract String getInterfaceName();

	protected abstract String getCallName();

	protected String getVersion() {
		return "v0001";
	}

	public R fetch(List<String> args) {
		Preconditions.checkNotNull(args);
		return fetch(args.toArray(new String[args.size()]));
	}

	protected R fetchRestTemplate(Class<R> clazz, String parameters) {
		return restTemplate.getForObject(getURL(parameters), clazz);
	}

	protected String getURL(String parameters) {
		return String.format("https://api.steampowered.com/%s/%s/%s/?key=%s%s",
				getInterfaceName(), getCallName(), getVersion(), steamTrackerConfig.getSteam().getApiKey(), parameters);
	}

}