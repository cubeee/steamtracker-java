package com.x7ff.steam;

import com.x7ff.steam.tool.Tool;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@EnableBatchProcessing
@ComponentScan(excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = Tool.class)
})
public class SteamTracker {

	public static void main(String[] args) {
		SpringApplication.run(SteamTracker.class, args);
	}

}