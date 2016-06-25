package com.x7ff.steam.task;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

import com.x7ff.steam.batch.PlayerBatchUtils;
import com.x7ff.steam.batch.snapshots.PlayerSnapshotBatch;
import com.x7ff.steam.config.SteamTrackerConfig;
import org.springframework.batch.core.Job;
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
			PlayerBatchUtils.asyncJobLauncher(
					snapshotBatch.getJobRepository(), "PlayerSnapshotUpdate")
					.run(job, PlayerBatchUtils.getTimeJobParameters());
		} catch (Exception e) {
			logger.log(Level.WARNING, "Failed to update snapshots", e);
		}
	}

	public long getDelay() {
		return TimeUnit.MINUTES.toMillis(steamTrackerConfig.getUpdater().getAutomaticUpdateInterval());
	}

}