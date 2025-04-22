package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.RequestCreateHitDto;
import ru.yandex.practicum.dto.RequestStatDto;
import ru.yandex.practicum.dto.StatHitDto;

import java.util.List;

public interface StatsService {
    void saveHit(RequestCreateHitDto hitDto);

    List<StatHitDto> getStats(RequestStatDto paramsDto);
}
