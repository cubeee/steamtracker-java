package com.x7ff.steam.batch.snapshots;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

import com.x7ff.steam.batch.PlayerBatchUtils;
import com.x7ff.steam.config.SteamTrackerConfig;
import com.x7ff.steam.domain.Player;
import com.x7ff.steam.service.steam.SteamPlayerService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
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

	public Job playerProcessJob() {
		return jobBuilderFactory.get("player_update_job")
				.repository(jobRepository)
				.incrementer(new RunIdIncrementer())
				.flow(playerProcessStep())
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