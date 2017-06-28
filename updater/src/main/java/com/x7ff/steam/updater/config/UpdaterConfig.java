package com.x7ff.steam.updater.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import java.util.concurrent.TimeUnit;

@Component
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
@Validated
public class UpdaterConfig {

    /**
     * Flag for optionally disabling scheduled tasks from being started.
     * May be useful in development environment when doing frequent restarts.
     */
    @Value("${updater.disable-scheduled-tasks}")
    private boolean disableScheduledTasks;

    /**
     * Delay between automatic player updates in minutes. Delay is started after the previous update has finished.
     * Default interval is set at 12 hours.
     */
    @Min(1)
    @Value("${updater.automatic-update-interval}")
    private long automaticUpdateInterval = TimeUnit.HOURS.toMinutes(6);

    /**
     * "Minimum age of player snapshots in minutes before they need to be updated.
     * Default interval is set at 6 hours."
     */
    @Min(1)
    @Value("${updater.snapshot-update-interval}")
    private long snapshotUpdateInterval = TimeUnit.HOURS.toMinutes(6);

    /**
     * The amount of players to fetch from database at once for snapshot updating.
     */
    @Min(1)
    @Value("${updater.snapshots-page-size}")
    private int snapshotsPageSize = 100;

    /**
     * The amount of players to process before saving for snapshot updating.
     */
    @Min(1)
    @Value("${updater.snapshots-chunk-size}")
    private int snapshotsChunkSize = 100;

    public boolean isDisableScheduledTasks() {
        return disableScheduledTasks;
    }

    public long getAutomaticUpdateInterval() {
        return automaticUpdateInterval;
    }

    public long getSnapshotUpdateInterval() {
        return snapshotUpdateInterval;
    }

    public int getSnapshotsPageSize() {
        return snapshotsPageSize;
    }

    public int getSnapshotsChunkSize() {
        return snapshotsChunkSize;
    }

}