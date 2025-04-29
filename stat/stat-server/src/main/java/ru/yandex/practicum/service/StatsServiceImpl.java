package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.mappers.StatMapper;
import ru.yandex.practicum.dao.model.Hit;
import ru.yandex.practicum.dto.RequestCreateHitDto;
import ru.yandex.practicum.dto.RequestStatDto;
import ru.yandex.practicum.dto.StatHitDto;
import ru.yandex.practicum.exception.InvalidArgumentException;
import ru.yandex.practicum.repository.HitRepository;


import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {

    private final HitRepository hitRepository;

    @Override
    @Transactional
    public void saveHit(RequestCreateHitDto hitDto) {
        Hit hit = new Hit();
        hit.setApp(hitDto.getApp());
        hit.setIp(hitDto.getIp());
        hit.setUri(hitDto.getUri());
        hit.setTimestamp(hitDto.getTimestamp());

        hitRepository.save(hit);
    }

    @Override
    @Transactional
    public List<StatHitDto> getStats(RequestStatDto paramsDto) {

        if (paramsDto.getStart().isAfter(paramsDto.getEnd())) {
            throw new InvalidArgumentException("Start date cannot be after end date");
        }

        if (paramsDto.getUris() == null || paramsDto.getUris().isEmpty()) {
            return (paramsDto.isUnique()
                    ? hitRepository.getStatsByDatesUnique(paramsDto.getStart(), paramsDto.getEnd())
                    : hitRepository.getStatsByDates(paramsDto.getStart(), paramsDto.getEnd()))
                    .stream()
                    .map(StatMapper::toStatDto)
                    .toList();
        }

        return (paramsDto.isUnique()
                ? hitRepository.getStatsByDatesAndUriUnique(paramsDto.getStart(), paramsDto.getEnd(), paramsDto.getUris())
                : hitRepository.getStatsByDatesAndUri(paramsDto.getStart(), paramsDto.getEnd(), paramsDto.getUris()))
                .stream()
                .map(StatMapper::toStatDto)
                .toList();
    }
}
