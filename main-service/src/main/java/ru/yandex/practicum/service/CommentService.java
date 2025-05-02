package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.comment.CommentDto;
import ru.yandex.practicum.dto.comment.UserCreateCommentDto;
import ru.yandex.practicum.dto.comment.UserUpdateCommentDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> getListByEvent(Long eventId, int from, int size);

    CommentDto create(Long eventId, Long authorId, UserCreateCommentDto dto);

    CommentDto update(Long eventId, Long authorId, UserUpdateCommentDto dto);

    void deleteByAuthor(Long eventId, Long authorId, Long id);

    void deleteByAdmin(Long id);
}
