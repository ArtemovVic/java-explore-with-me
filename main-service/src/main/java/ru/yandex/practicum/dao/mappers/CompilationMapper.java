package ru.yandex.practicum.dao.mappers;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.dao.model.Compilation;
import ru.yandex.practicum.dto.compilation.CompilationDto;

import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {
    public static CompilationDto toDto(
            final Compilation compilation,
            Map<Long, Integer> views
    ) {
        return CompilationDto
                .builder()
                .id(compilation.getId())
                .title(compilation.getName())
                .pinned(compilation.isPinned())
                .events(compilation.getEvents().stream().map(
                        el -> EventMapper.toShortDto(
                                el,
                                views.getOrDefault(el.getId(), 0)
                        )
                ).collect(Collectors.toSet()))
                .build();
    }
}
