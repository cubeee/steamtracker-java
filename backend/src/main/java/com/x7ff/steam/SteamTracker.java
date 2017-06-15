package com.x7ff.steam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SteamTracker {

	public static void main(String[] args) {
		SpringApplication.run(SteamTracker.class, args);
	}

}