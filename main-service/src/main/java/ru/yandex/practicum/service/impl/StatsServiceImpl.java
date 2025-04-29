package ru.yandex.practicum.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.StatsClient;
import ru.yandex.practicum.dto.RequestCreateHitDto;
import ru.yandex.practicum.dto.StatGetRequestDto;
import ru.yandex.practicum.dto.StatHitDto;
import ru.yandex.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@ComponentScan(basePackages = "ru.practicum")
public class StatsServiceImpl implements StatsService {
    private static final LocalDateTime DATE_TIME_MIN = LocalDateTime.of(1970, 1, 1, 0, 0);

    private final StatsClient client;
    private final String appName;

    StatsServiceImpl(
            StatsClient client,
            @Value("${app.name}")
            String appName
    ) {
        this.client = client;
        this.appName = appName;
    }

    public Map<String, Integer> getViewsCount(Set<String> uris) {
        return getCount(uris, false);
    }

    public Map<String, Integer> getUniqueViewsCount(Set<String> uris) {
        return getCount(uris, true);
    }

    private Map<String, Integer> getCount(Set<String> uris, boolean unique) {
        StatGetRequestDto request = new StatGetRequestDto();

        request.setStart(DATE_TIME_MIN);
        request.setEnd(LocalDateTime.now());
        request.setUris(uris);
        request.setUnique(unique);

        List<StatHitDto> hits = client.getStats(request);
        Map<String, Integer> hitMap = new HashMap<>();

        for (StatHitDto hitStat : hits) {
            hitMap.put(hitStat.getUri(), hitStat.getHits());
        }

        return hitMap;
    }

    public void addHit(String uri, String ip) {
        RequestCreateHitDto request = new RequestCreateHitDto();
        request.setUri(uri);
        request.setApp(appName);
        request.setIp(ip);
        request.setTimestamp(LocalDateTime.now());

        client.saveHit(request);
    }
}
