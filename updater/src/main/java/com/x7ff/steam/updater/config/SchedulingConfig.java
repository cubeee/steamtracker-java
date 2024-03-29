package com.x7ff.steam.updater.config;

import com.x7ff.steam.updater.task.ScheduledProfileUpdateTask;
import com.x7ff.steam.updater.task.ScheduledSnapshotUpdateTask;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import javax.inject.Inject;

@Configuration
@EnableScheduling
public class SchedulingConfig implements SchedulingConfigurer {

    private final UpdaterConfig updaterConfig;

    private final ScheduledSnapshotUpdateTask snapshotUpdateTask;

    private final ScheduledProfileUpdateTask profileUpdateTask;

    @Inject
    public SchedulingConfig(UpdaterConfig updaterConfig, ScheduledSnapshotUpdateTask snapshotUpdateTask, ScheduledProfileUpdateTask profileUpdateTask) {
        this.updaterConfig = updaterConfig;
        this.snapshotUpdateTask = snapshotUpdateTask;
        this.profileUpdateTask = profileUpdateTask;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        if (updaterConfig.isDisableScheduledTasks()) {
            return;
        }
        taskRegistrar.addFixedDelayTask(snapshotUpdateTask, snapshotUpdateTask.getDelay());
        taskRegistrar.addTriggerTask(profileUpdateTask, new CronTrigger("0 0 0 * * *"));
    }

}