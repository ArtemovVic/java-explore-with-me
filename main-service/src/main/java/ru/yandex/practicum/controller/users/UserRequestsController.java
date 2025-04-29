package ru.yandex.practicum.controller.users;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.event.EventRequestDto;
import ru.yandex.practicum.service.EventRequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@Validated
@RequiredArgsConstructor
public class UserRequestsController {

    private final EventRequestService eventRequestService;

    @GetMapping
    public List<EventRequestDto> getList(
            @PathVariable @Positive Long userId
    ) {
        return eventRequestService.getListByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventRequestDto create(
            @PathVariable @Positive Long userId,
            @RequestParam @Positive Long eventId
    ) {
        return eventRequestService.create(eventId, userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public EventRequestDto update(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long requestId
    ) {
        return eventRequestService.cancel(requestId, userId);
    }
}
