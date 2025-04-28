package ru.yandex.practicum.service.util;

import java.util.Map;
import java.util.Set;

public interface StatsService {
    Map<String, Integer> getViewsCount(Set<String> uris);

    Map<String, Integer> getUniqueViewsCount(Set<String> uris);

    void addHit(String uri, String ip);
}
