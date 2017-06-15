package com.x7ff.steam.updater.batch.snapshots;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;

/**
 * Checks if at least {@link LastRunJobExecutionListener#intervalMinutes} has passed since
 * the last completed job with the name {@link LastRunJobExecutionListener#jobName}
 * and stops the {@code jobExecution} of {@link LastRunJobExecutionListener#beforeJob(JobExecution)} if enough time has not passed.
 */
public final class LastRunJobExecutionListener implements JobExecutionListener {

	/**
	 * Name of the job to check
	 */
	private final String jobName;

	/**
	 * Minimum interval in minutes between completed jobs
	 */
	private final long intervalMinutes;

	private final JobExplorer jobExplorer;

	public LastRunJobExecutionListener(String jobName, JobExplorer jobExplorer, long intervalMinutes) {
		this.jobName = jobName;
		this.jobExplorer = jobExplorer;
		this.intervalMinutes = intervalMinutes;
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
		if (jobExecution == null) {
			return;
		}

		List<JobInstance> jobInstances = jobExplorer.getJobInstances(jobName, 0, Short.MAX_VALUE);
		for (JobInstance jobInstance : jobInstances) {
			JobExecution execution = jobExplorer.getJobExecution(jobInstance.getInstanceId());
			if (execution == null) {
				continue;
			}
			String exitCode = execution.getExitStatus().getExitCode();
			if (exitCode != null && !exitCode.equals(ExitStatus.COMPLETED.getExitCode())) {
				continue;
			}
			Instant endTime = execution.getEndTime().toInstant();
			Instant nextUpdate = endTime.plus(Duration.ofMinutes(intervalMinutes));
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