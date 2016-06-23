package com.x7ff.steam;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class SteamTracker {

	public static void main(String[] args) {
		SpringApplication.run(SteamTracker.class, args);
	}

}