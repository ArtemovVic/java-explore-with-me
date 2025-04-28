package ru.yandex.practicum.dao.mappers;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.dao.model.EventRequest;
import ru.yandex.practicum.dto.event.EventRequestDto;

@UtilityClass
public class EventRequestMapper {
    public static EventRequestDto toDto(EventRequest request) {
        return EventRequestDto
                .builder()
                .id(request.getId())
                .requester(request.getUser().getId())
                .event(request.getEvent().getId())
                .status(request.getState())
                .created(request.getCreated())
                .build();
    }
}
