package com.x7ff.steam.config;

import javax.inject.Inject;

import com.x7ff.steam.task.ScheduledProfileUpdateTask;
import com.x7ff.steam.task.ScheduledSnapshotUpdateTask;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

@Configuration
@EnableScheduling
public class SchedulingConfig implements SchedulingConfigurer {

	@Inject
	private ScheduledSnapshotUpdateTask snapshotUpdateTask;

	@Inject
	private ScheduledProfileUpdateTask profileUpdateTask;

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.addFixedDelayTask(snapshotUpdateTask, snapshotUpdateTask.getDelay());
		taskRegistrar.addTriggerTask(profileUpdateTask, new CronTrigger("0 0 0 * * *"));
	}

}