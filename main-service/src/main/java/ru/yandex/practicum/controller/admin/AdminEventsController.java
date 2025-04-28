package ru.yandex.practicum.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.admin.event.AdminSearchEventDto;
import ru.yandex.practicum.dto.admin.event.AdminUpdateEventDto;
import ru.yandex.practicum.dto.event.EventDto;
import ru.yandex.practicum.service.util.EventService;

import java.util.List;

@RestController
@RequestMapping("/admin/events")
@Validated
@RequiredArgsConstructor
public class AdminEventsController {
    private final EventService eventService;

    @GetMapping
    public List<EventDto> getList(
            @Valid AdminSearchEventDto searchDto
    ) {
        return eventService.getListForAdmin(searchDto);
    }

    @PatchMapping("/{eventId}")
    public EventDto update(
            @PathVariable @Positive Long eventId,
            @RequestBody @Valid AdminUpdateEventDto dto
    ) {
        return eventService.updateByAdmin(eventId, dto);
    }
}
