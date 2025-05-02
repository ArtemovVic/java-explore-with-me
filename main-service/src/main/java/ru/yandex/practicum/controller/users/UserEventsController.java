package ru.yandex.practicum.controller.users;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dao.model.constant.Constant;
import ru.yandex.practicum.dto.comment.CommentDto;
import ru.yandex.practicum.dto.comment.UserCreateCommentDto;
import ru.yandex.practicum.dto.comment.UserUpdateCommentDto;
import ru.yandex.practicum.dto.event.*;
import ru.yandex.practicum.service.CommentService;
import ru.yandex.practicum.service.EventRequestService;
import ru.yandex.practicum.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@Validated
@RequiredArgsConstructor
public class UserEventsController {

    private final EventService eventService;
    private final EventRequestService eventRequestService;
    private final CommentService commentService;

    @GetMapping
    public List<EventDto> getList(
            @PathVariable @Positive Long userId,
            @RequestParam(defaultValue = Constant.INT_MIN_STRING) @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size
    ) {
        return eventService.getListByUser(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto create(
            @PathVariable @Positive Long userId,
            @RequestBody @Valid UserCreateEventDto dto
    ) {
        return eventService.createByUser(userId, dto);
    }

    @GetMapping("/{eventId}")
    public EventDto getById(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId
    ) {
        return eventService.getByIdAndUser(eventId, userId);
    }

    @PatchMapping("/{eventId}")
    public EventDto update(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId,
            @RequestBody @Valid UserUpdateEventDto dto
    ) {
        return eventService.updateByUser(eventId, userId, dto);
    }

    @GetMapping("/{eventId}/requests")
    public List<EventRequestDto> getRequestsList(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId
    ) {
        return eventRequestService.getRequestsListByEventIdAndUser(eventId, userId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestResultDto update(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId,
            @RequestBody @Valid UpdateEventRequestsDto dto
    ) {
        return eventRequestService.updateByEventIdAndStatusId(eventId, userId, dto);
    }

    @PostMapping("/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId,
            @RequestBody @Valid UserCreateCommentDto dto
    ) {
        return commentService.create(eventId, userId, dto);
    }

    @PatchMapping("/{eventId}/comments/{commentId}")
    public CommentDto updateComment(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId,
            @PathVariable @Positive Long commentId,
            @RequestBody @Valid UserUpdateCommentDto dto
    ) {
        dto.setId(commentId);

        return commentService.update(eventId, userId, dto);
    }

    @DeleteMapping("/{eventId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId,
            @PathVariable @Positive Long commentId
    ) {
        commentService.deleteByAuthor(eventId, userId, commentId);
    }
}
