package com.x7ff.steam.config;

import javax.inject.Inject;

import com.x7ff.steam.task.ScheduledSnapshotUpdateTask;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
public class SchedulingConfig implements SchedulingConfigurer {

	@Inject
	private ScheduledSnapshotUpdateTask snapshotUpdateTask;

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.addFixedDelayTask(snapshotUpdateTask, snapshotUpdateTask.getDelay());
	}

}