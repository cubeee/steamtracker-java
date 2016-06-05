package com.x7ff.steam.service.steam;

import com.x7ff.steam.config.SteamConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public abstract class SteamService<R> {

	protected final SteamConfig steamConfig;

	protected final RestTemplate restTemplate;

	@Autowired
	public SteamService(SteamConfig steamConfig) {
		this.steamConfig = steamConfig;
		this.restTemplate = new RestTemplate();
	}

	public abstract R fetch(String... args);

	protected abstract String getInterfaceName();

	protected abstract String getCallName();

	protected String getVersion() {
		return "v0001";
	}

	protected R fetchRestTemplate(Class<R> clazz, String parameters) {
		return restTemplate.getForObject(getURL(parameters), clazz);
	}

	protected String getURL(String parameters) {
		String url = "https://api.steampowered.com/" + getInterfaceName() + "/" + getCallName() + "/" + getVersion() + "/?key=" + steamConfig.getApiKey() + parameters;
		System.out.println(url);
		return url;
	}

}