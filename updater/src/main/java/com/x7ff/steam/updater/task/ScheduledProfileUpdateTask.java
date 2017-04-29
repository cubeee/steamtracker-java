package com.x7ff.steam.updater.task;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;

import com.x7ff.steam.updater.batch.PlayerBatchUtils;
import com.x7ff.steam.updater.batch.profile.PlayerProfileBatch;
import org.springframework.batch.core.Job;
import org.springframework.stereotype.Component;

@Component
public final class ScheduledProfileUpdateTask implements Runnable {

	private static final Logger logger = Logger.getLogger(ScheduledSnapshotUpdateTask.class.getName());

	@Inject
	private PlayerProfileBatch profileBatch;

	@Override
	public void run() {
		Job job = profileBatch.profileProcessJob();
		try {
			PlayerBatchUtils.asyncJobLauncher(
					profileBatch.getJobRepository(), "PlayerProfileBatchUpdate")
					.run(job, PlayerBatchUtils.getTimeJobParameters());
		} catch (Exception e) {
			logger.log(Level.WARNING, "Failed to update snapshots", e);
		}
	}
}