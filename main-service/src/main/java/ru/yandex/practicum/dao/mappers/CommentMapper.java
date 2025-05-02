package ru.yandex.practicum.dao.mappers;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.dao.model.Comment;
import ru.yandex.practicum.dto.comment.CommentDto;

@UtilityClass
public class CommentMapper {

    public static CommentDto toDto(final Comment comment) {
        return CommentDto
                .builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(
                        new CommentDto.AuthorDto(
                                comment.getUser().getId(),
                                comment.getUser().getName()
                        )
                )
                .created(comment.getCreated())
                .updated(comment.getUpdated())
                .build();
    }
}
