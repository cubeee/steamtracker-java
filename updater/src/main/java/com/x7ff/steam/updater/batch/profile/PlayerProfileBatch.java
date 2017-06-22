package com.x7ff.steam.updater.batch.profile;

import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

import com.x7ff.steam.shared.domain.Player;
import com.x7ff.steam.shared.service.steam.SteamPlayerService;
import com.x7ff.steam.updater.batch.PlayerBatchUtils;
import com.x7ff.steam.updater.util.JpaPagingItemListReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class PlayerProfileBatch {

    @Inject
    private JobBuilderFactory jobBuilderFactory;

    @Inject
    private StepBuilderFactory stepBuilderFactory;

    @Inject
    private JobRepository jobRepository;

    @Inject
    private EntityManagerFactory entityManagerFactory;

    @Inject
    private PlayerListWriter playerListWriter;

    @Inject
    private PlayerProfileBatchProcessor batchProcessor;

    public Job profileProcessJob() {
        return jobBuilderFactory.get("profile_update_job")
                .repository(jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(updateStep())
                .end()
                .build();
    }

    private Step updateStep() {
        return stepBuilderFactory.get("process_player")
                .<List<Player>, List<Player>>chunk(SteamPlayerService.MAX_PROFILE_BATCH_SIZE)
                .reader(playerReader())
                .processor(batchProcessor)
                .writer(playerWriter())
                .build();
    }

    private JpaPagingItemListReader<Player> playerReader() {
        return PlayerBatchUtils.getPlayerListReader(SteamPlayerService.MAX_PROFILE_BATCH_SIZE, entityManagerFactory);
    }

    private ItemWriter<List<Player>> playerWriter() {
        return playerListWriter;
    }

    public JobRepository getJobRepository() {
        return jobRepository;
    }

}