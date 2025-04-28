package ru.yandex.practicum.service.util;

import ru.yandex.practicum.dto.event.EventRequestDto;
import ru.yandex.practicum.dto.event.EventRequestResultDto;
import ru.yandex.practicum.dto.event.UpdateEventRequestsDto;

import java.util.List;

public interface EventRequestService {
    List<EventRequestDto> getRequestsListByEventIdAndUser(Long eventId, Long userId);

    EventRequestResultDto updateByEventIdAndStatusId(Long eventId, Long userId, UpdateEventRequestsDto dto);

    List<EventRequestDto> getListByUserId(Long userId);

    EventRequestDto create(Long eventId, Long userId);

    EventRequestDto cancel(Long id, Long userId);
}
