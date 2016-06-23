package com.x7ff.steam.task;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

import com.google.common.collect.Maps;
import com.x7ff.steam.batch.snapshots.PlayerSnapshotBatch;
import com.x7ff.steam.config.SteamTrackerConfig;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.stereotype.Component;

@Component
public final class ScheduledSnapshotUpdateTask implements Runnable {

	private static final Logger logger = Logger.getLogger(ScheduledSnapshotUpdateTask.class.getName());

	@Inject
	private SteamTrackerConfig steamTrackerConfig;

	@Inject
	private PlayerSnapshotBatch snapshotBatch;

	@Override
	public void run() {
		Job job = snapshotBatch.playerProcessJob();
		try {
			snapshotBatch.jobLauncher().run(job, getJobParameters());
		} catch (Exception e) {
			logger.log(Level.WARNING, "Failed to update snapshots", e);
		}
	}

	public long getDelay() {
		return TimeUnit.MINUTES.toMillis(steamTrackerConfig.getUpdater().getAutomaticUpdateInterval());
	}

	private JobParameters getJobParameters() {
		Map<String, JobParameter> parameters = Maps.newHashMap();
		parameters.put("time", new JobParameter(System.currentTimeMillis()));
		return new JobParameters(parameters);
	}

}