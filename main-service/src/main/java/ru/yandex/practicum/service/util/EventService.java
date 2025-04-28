package ru.yandex.practicum.service.util;

import ru.yandex.practicum.dao.model.Event;
import ru.yandex.practicum.dto.admin.event.AdminSearchEventDto;
import ru.yandex.practicum.dto.admin.event.AdminUpdateEventDto;
import ru.yandex.practicum.dto.event.EventDto;
import ru.yandex.practicum.dto.event.SearchEventDto;
import ru.yandex.practicum.dto.event.UserCreateEventDto;
import ru.yandex.practicum.dto.event.UserUpdateEventDto;

import java.util.List;

public interface EventService {
    List<EventDto> getList(SearchEventDto searchDto);

    List<EventDto> getListForAdmin(AdminSearchEventDto searchDto);

    List<EventDto> getListByUser(Long userId, int from, int size);

    EventDto getById(Long id);

    EventDto getByIdAndUser(Long id, Long userId);

    EventDto updateByAdmin(Long id, AdminUpdateEventDto dto);

    EventDto createByUser(Long userId, UserCreateEventDto dto);

    EventDto updateByUser(Long id, Long userId, UserUpdateEventDto dto);

    String getUrl(Event event);

    Long getIdFromUrl(String url);
}
