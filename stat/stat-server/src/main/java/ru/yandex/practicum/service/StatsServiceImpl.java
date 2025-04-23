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
import ru.yandex.practicum.dto.ViewStats;
import ru.yandex.practicum.repository.HitRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {

    private final HitRepository hitRepository;
    private final StatMapper statMapper;

    @Override
    @Transactional
    public void saveHit(RequestCreateHitDto hitDto) {
        Hit hit = new Hit();
        hit.setApp(hitDto.getApp());
        hit.setIp(hitDto.getIp());
        hit.setUri(hitDto.getUri());
        hit.setTimestamp(hitDto.getTimestamp());

        hitRepository.save(hit);
        log.info("Saved hit: {}", hit);
    }

    @Override
    @Transactional
    public List<StatHitDto> getStats(RequestStatDto dto) {

        List<ViewStats> stats = hitRepository.findStats(
                dto.getStart(),
                dto.getEnd(),
                dto.getUris() != null && !dto.getUris().isEmpty() ? dto.getUris() : null,
                dto.isUnique()
        );

        return stats.stream()
                .map(statMapper::toStatDto)
                .toList();
    }
}
