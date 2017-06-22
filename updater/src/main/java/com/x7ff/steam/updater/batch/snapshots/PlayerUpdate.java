package com.x7ff.steam.updater.batch.snapshots;

import com.x7ff.steam.shared.domain.Player;

public final class PlayerUpdate {

    private final Player player;

    private final boolean updated;

    public PlayerUpdate(Player player, boolean updated) {
        this.player = player;
        this.updated = updated;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isUpdated() {
        return updated;
    }

}