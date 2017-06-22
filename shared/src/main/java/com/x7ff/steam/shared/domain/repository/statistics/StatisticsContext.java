package com.x7ff.steam.shared.domain.repository.statistics;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public final class StatisticsContext {

    private final Map<String, Object> items = Maps.newHashMap();

    public <T> StatisticsContext addItem(String key, T item) {
        Preconditions.checkNotNull(item, "Item can't be null");
        items.put(key, item);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T getItem(String key) {
        return (T) items.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getItem(String key, T defaultItem) {
        return (T) items.getOrDefault(key, defaultItem);
    }

    public boolean deleteItem(String key) {
        return items.remove(key) != null;
    }

}