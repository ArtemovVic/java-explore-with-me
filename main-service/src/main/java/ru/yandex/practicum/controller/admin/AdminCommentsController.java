package ru.yandex.practicum.controller.admin;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.service.CommentService;

@RestController
@RequestMapping("/admin/comments")
@Validated
@RequiredArgsConstructor
public class AdminCommentsController {

    private final CommentService commentService;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive final Long id) {
        commentService.deleteByAdmin(id);
    }

}
