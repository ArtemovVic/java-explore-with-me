package ru.yandex.practicum.dao.mappers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.StatHitDto;
import ru.yandex.practicum.dto.ViewStats;

@Component
public class StatMapper {

    public static StatHitDto toStatDto(ViewStats dto) {
        return StatHitDto.builder()
                .app(dto.getApp())
                .uri(dto.getUri())
                .hits(dto.getHits())
                .build();
    }
}
