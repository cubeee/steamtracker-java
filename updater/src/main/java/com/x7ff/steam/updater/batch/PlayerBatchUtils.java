package com.x7ff.steam.updater.batch;

import java.util.Map;
import javax.persistence.EntityManagerFactory;

import com.google.common.collect.Maps;
import com.x7ff.steam.shared.domain.Player;
import com.x7ff.steam.updater.util.JpaPagingItemListReader;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

public final class PlayerBatchUtils {

    public static JpaPagingItemReader<Player> getPlayerReader(int pageSize, EntityManagerFactory entityManagerFactory) {
        JpaPagingItemReader<Player> reader = new JpaPagingItemReader<>();
        reader.setQueryString("SELECT p FROM Player p");
        reader.setPageSize(pageSize);
        reader.setEntityManagerFactory(entityManagerFactory);
        return reader;
    }

    public static JpaPagingItemListReader<Player> getPlayerListReader(int pageSize, EntityManagerFactory entityManagerFactory) {
        JpaPagingItemListReader<Player> reader = new JpaPagingItemListReader<>();
        reader.setQueryString("SELECT p FROM Player p");
        reader.setPageSize(pageSize);
        reader.setEntityManagerFactory(entityManagerFactory);
        return reader;
    }

    public static SimpleJobLauncher asyncJobLauncher(JobRepository jobRepository, String threadNamePrefix) throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor(threadNamePrefix));
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    public static JobParameters getTimeJobParameters() {
        Map<String, JobParameter> parameters = Maps.newHashMap();
        parameters.put("time", new JobParameter(System.currentTimeMillis()));
        return new JobParameters(parameters);
    }

    private PlayerBatchUtils() {

    }

}