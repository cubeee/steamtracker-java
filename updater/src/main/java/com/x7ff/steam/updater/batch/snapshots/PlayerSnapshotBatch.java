package com.x7ff.steam.updater.batch.snapshots;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

import com.x7ff.steam.shared.config.SteamTrackerConfig;
import com.x7ff.steam.shared.domain.Player;
import com.x7ff.steam.shared.domain.repository.statistics.MostPlayedGamesStatistics;
import com.x7ff.steam.shared.service.steam.SteamPlayerService;
import com.x7ff.steam.updater.batch.PlayerBatchUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class PlayerSnapshotBatch {

	@Inject
	private SteamTrackerConfig steamTrackerConfig;

	@Inject
	private SteamPlayerService steamPlayerService;

	@Inject
	private JobBuilderFactory jobBuilderFactory;

	@Inject
	private StepBuilderFactory stepBuilderFactory;

	@Inject
	private EntityManagerFactory entityManagerFactory;

	@Inject
	private JobRepository jobRepository;

	@Inject
	private PlayerWriter playerWriter;

	@Inject
	private JobExplorer jobExplorer;

	@Inject
	@Qualifier("mostPlayed")
	private MostPlayedGamesStatistics mostPlayedGamesStatistics;

	public Job playerProcessJob() {
		String jobName = "player_update_job";
		return jobBuilderFactory.get(jobName)
				.repository(jobRepository)
				.listener(new LastRunJobExecutionListener(jobName, jobExplorer, steamTrackerConfig.getUpdater()
						.getSnapshotUpdateInterval()))
				.incrementer(new RunIdIncrementer())
				.flow(playerProcessStep())
				.next(cachePopulatorStep())
				.end()
				.build();
	}

	private Step playerProcessStep() {
		return stepBuilderFactory.get("process_player")
				.<Player, PlayerUpdate>chunk(steamTrackerConfig.getUpdater().getSnapshotsChunkSize())
				.reader(playerReader())
				.processor(playerProcessor())
				.writer(playerWriter())
				.build();
	}

	@Bean
	private TaskletStep cachePopulatorStep() {
		return stepBuilderFactory.get("cache_populator").tasklet((contribution, chunkContext) -> {
			mostPlayedGamesStatistics.refreshCache();
			return RepeatStatus.FINISHED;
		}).build();
	}

	private JpaPagingItemReader<Player> playerReader() {
		return PlayerBatchUtils.getPlayerReader(steamTrackerConfig.getUpdater().getSnapshotsPageSize(), entityManagerFactory);
	}

	private ItemWriter<PlayerUpdate> playerWriter() {
		return playerWriter;
	}

	private PlayerSnapshotUpdateProcessor playerProcessor() {
		return new PlayerSnapshotUpdateProcessor(steamTrackerConfig, steamPlayerService);
	}

	public JobRepository getJobRepository() {
		return jobRepository;
	}

}