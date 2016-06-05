package com.x7ff.steam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class SteamTracker {

	public static void main(String[] args) {
		SpringApplication.run(SteamTracker.class, args);
	}

}