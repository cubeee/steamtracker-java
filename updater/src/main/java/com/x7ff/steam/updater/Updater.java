package com.x7ff.steam.updater;

import com.x7ff.steam.updater.tool.Tool;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.x7ff.steam")
@EnableBatchProcessing
@EnableCaching
@EnableJpaRepositories("com.x7ff.steam")
@EntityScan(basePackages = { "com.x7ff.steam" })
@ComponentScan(
		basePackages = { "com.x7ff.steam" },
		excludeFilters = { @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = Tool.class) }
)
public class Updater {

	public static void main(String[] args) {
		SpringApplication.run(Updater.class, args);
	}

}