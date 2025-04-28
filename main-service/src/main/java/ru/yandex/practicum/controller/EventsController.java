package ru.yandex.practicum.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.event.EventDto;
import ru.yandex.practicum.dto.event.SearchEventDto;
import ru.yandex.practicum.service.util.EventService;
import ru.yandex.practicum.service.util.StatsService;

import java.util.List;

@RestController
@RequestMapping("/events")
@Validated
@RequiredArgsConstructor
public class EventsController {
    private final EventService eventService;
    private final StatsService statsService;

    @GetMapping
    public List<EventDto> getList(
            @Valid SearchEventDto searchEventDto,
            HttpServletRequest request
    ) {
        List<EventDto> res = eventService.getList(searchEventDto);

        statsService.addHit(request.getRequestURI(), request.getRemoteAddr());

        return res;
    }

    @GetMapping("/{id}")
    public EventDto getById(@PathVariable Long id, HttpServletRequest request) {
        EventDto res = eventService.getById(id);

        statsService.addHit(request.getRequestURI(), request.getRemoteAddr());

        return res;
    }
}
