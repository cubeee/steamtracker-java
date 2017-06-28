package com.x7ff.steam.shared.service.steam;

import com.google.common.base.Preconditions;
import com.x7ff.steam.shared.config.SteamConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.List;

@Service
public abstract class SteamService<R> {

    protected final SteamConfig steamConfig;

    protected final RestTemplate restTemplate;

    @Inject
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

    public R fetch(List<String> args) {
        Preconditions.checkNotNull(args);
        return fetch(args.toArray(new String[args.size()]));
    }

    protected R fetchRestTemplate(Class<R> clazz, String parameters) {
        String url = getURL(parameters);
        return restTemplate.getForObject(url, clazz);
    }

    protected String getURL(String parameters) {
        return String.format("https://api.steampowered.com/%s/%s/%s/?key=%s%s",
                getInterfaceName(), getCallName(), getVersion(), steamConfig.getApiKey(), parameters);
    }

}