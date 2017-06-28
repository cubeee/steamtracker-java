package com.x7ff.steam.updater.batch.snapshots;

import com.x7ff.steam.shared.domain.Player;
import com.x7ff.steam.shared.service.steam.SteamPlayerService;
import com.x7ff.steam.updater.batch.PlayerBatchUtils;
import com.x7ff.steam.updater.config.UpdaterConfig;
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
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

@Component
public class PlayerSnapshotBatch {

    private final UpdaterConfig updaterConfig;
    private final SteamPlayerService steamPlayerService;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final JobRepository jobRepository;
    private final PlayerWriter playerWriter;
    private final JobExplorer jobExplorer;

    @Inject
    public PlayerSnapshotBatch(UpdaterConfig updaterConfig, SteamPlayerService steamPlayerService,
                               JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
                               EntityManagerFactory entityManagerFactory, JobRepository jobRepository,
                               PlayerWriter playerWriter, JobExplorer jobExplorer) {
        this.updaterConfig = updaterConfig;
        this.steamPlayerService = steamPlayerService;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
        this.jobRepository = jobRepository;
        this.playerWriter = playerWriter;
        this.jobExplorer = jobExplorer;
    }

    public Job playerProcessJob() {
        String jobName = "player_update_job";
        return jobBuilderFactory.get(jobName)
                .repository(jobRepository)
                .listener(new LastRunJobExecutionListener(jobName, jobExplorer, updaterConfig
                        .getSnapshotUpdateInterval()))
                .incrementer(new RunIdIncrementer())
                .flow(playerProcessStep())
                .next(cachePopulatorStep())
                .end()
                .build();
    }

    private Step playerProcessStep() {
        return stepBuilderFactory.get("process_player")
                .<Player, PlayerUpdate>chunk(updaterConfig.getSnapshotsChunkSize())
                .reader(playerReader())
                .processor(playerProcessor())
                .writer(playerWriter())
                .build();
    }

    @Bean
    private TaskletStep cachePopulatorStep() {
        return stepBuilderFactory.get("cache_populator").tasklet((contribution, chunkContext) -> {
            // TODO: refresh cache
            return RepeatStatus.FINISHED;
        }).build();
    }

    private JpaPagingItemReader<Player> playerReader() {
        return PlayerBatchUtils.getPlayerReader(updaterConfig.getSnapshotsPageSize(), entityManagerFactory);
    }

    private ItemWriter<PlayerUpdate> playerWriter() {
        return playerWriter;
    }

    private PlayerSnapshotUpdateProcessor playerProcessor() {
        return new PlayerSnapshotUpdateProcessor(updaterConfig, steamPlayerService);
    }

    public JobRepository getJobRepository() {
        return jobRepository;
    }

}