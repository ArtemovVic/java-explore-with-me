package ru.yandex.practicum.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dao.model.constant.Constant;
import ru.yandex.practicum.dto.comment.CommentDto;
import ru.yandex.practicum.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/events/{eventId}/comments")
@Validated
@RequiredArgsConstructor
public class CommentsController {

    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> getEventComments(
            @PathVariable @Positive Long eventId,
            @RequestParam(value = "from", defaultValue = Constant.INT_MIN_STRING) @PositiveOrZero int from,
            @RequestParam(value = "size", defaultValue = "10") @Positive int size
    ) {
        return commentService.getListByEvent(eventId, from, size);
    }

}
