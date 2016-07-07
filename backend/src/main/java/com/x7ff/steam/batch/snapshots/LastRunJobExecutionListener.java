package com.x7ff.steam.batch.snapshots;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import com.x7ff.steam.config.SteamTrackerConfig;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;

public final class LastRunJobExecutionListener implements JobExecutionListener {

	private final String jobName;

	private final JobExplorer jobExplorer;

	private final SteamTrackerConfig steamTrackerConfig;

	public LastRunJobExecutionListener(String jobName, JobExplorer jobExplorer, SteamTrackerConfig steamTrackerConfig) {
		this.jobName = jobName;
		this.jobExplorer = jobExplorer;
		this.steamTrackerConfig = steamTrackerConfig;
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
		if (jobExecution == null) {
			return;
		}

		List<JobInstance> jobInstances = jobExplorer.getJobInstances(jobName, 0, Short.MAX_VALUE);
		for (JobInstance jobInstance : jobInstances) {
			JobExecution execution = jobExplorer.getJobExecution(jobInstance.getInstanceId());
			if (!execution.getExitStatus().getExitCode().equals(ExitStatus.COMPLETED.getExitCode())) {
				continue;
			}
			Instant endTime = execution.getEndTime().toInstant();
			Instant nextUpdate = endTime.plus(Duration.ofMinutes(steamTrackerConfig.getUpdater().getSnapshotUpdateInterval()));
			if (nextUpdate.isAfter(Instant.now())) {
				jobExecution.stop();
				break;
			}
			break;
		}
	}

	@Override
	public void afterJob(JobExecution jobExecution) {

	}

}