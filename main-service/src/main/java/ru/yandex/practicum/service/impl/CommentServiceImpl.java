package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.mappers.CommentMapper;
import ru.yandex.practicum.dao.model.Comment;
import ru.yandex.practicum.dao.model.Event;
import ru.yandex.practicum.dao.model.User;
import ru.yandex.practicum.dao.model.constant.EventRequestState;
import ru.yandex.practicum.dao.model.constant.EventState;
import ru.yandex.practicum.dto.comment.CommentDto;
import ru.yandex.practicum.dto.comment.UserCreateCommentDto;
import ru.yandex.practicum.dto.comment.UserUpdateCommentDto;
import ru.yandex.practicum.exception.DataErrorException;
import ru.yandex.practicum.exception.LogicErrorException;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.EventRepository;
import ru.yandex.practicum.repository.EventRequestRepository;
import ru.yandex.practicum.repository.UserRepository;
import ru.yandex.practicum.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private static final int AUTHOR_DELETE_HOURS_LIMIT = 2;
    private static final int AUTHOR_UPDATE_HOURS_LIMIT = 1;

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final EventRequestRepository eventRequestRepository;
    private final UserRepository userRepository;

    @Override
    public List<CommentDto> getListByEvent(Long eventId, int from, int size) {

        checkEvent(eventId);

        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        Pageable pageable = PageRequest.of(from / size, size, sort);

        return commentRepository.findAllByEvent_Id(eventId, pageable)
                .stream()
                .map(CommentMapper::toDto)
                .toList();
    }

    @Override
    public CommentDto create(Long eventId, Long authorId, UserCreateCommentDto dto) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event not found")
        );

        if (!EventState.PUBLISHED.equals(event.getState())) {
            throw new LogicErrorException("Event is not published");
        }

        User user = userRepository.findById(authorId).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        if (
                !event.getUser().getId().equals(user.getId())
                        && !eventRequestRepository.existsByEvent_IdAndUser_IdAndStateIs(eventId, authorId, EventRequestState.CONFIRMED)
        ) {
            throw new LogicErrorException("The user is not the owner of the event or has not applied for participation");
        }

        Comment comment = new Comment();
        comment.setEvent(event);
        comment.setUser(user);
        comment.setText(dto.getText());

        try {
            commentRepository.save(comment);
        } catch (DataAccessException e) {
            throw new DataErrorException("DB error: " + e.getMessage());
        }

        return CommentMapper.toDto(comment);
    }

    @Override
    public CommentDto update(Long eventId, Long authorId, UserUpdateCommentDto dto) {
        checkEvent(eventId);
        checkUser(authorId);

        Comment comment = commentRepository.findById(dto.getId()).orElseThrow(
                () -> new NotFoundException("Comment not found")
        );

        if (!comment.getUser().getId().equals(authorId)) {
            throw new LogicErrorException("User don't own this comment");
        }

        if (comment.getCreated().isBefore(LocalDateTime.now().minusHours(AUTHOR_UPDATE_HOURS_LIMIT))) {
            throw new LogicErrorException("You can't delete this comment");
        }

        if (dto.getText() != null) {
            comment.setText(dto.getText());
        }

        try {
            commentRepository.save(comment);
        } catch (DataAccessException e) {
            throw new DataErrorException("DB error: " + e.getMessage());
        }

        return CommentMapper.toDto(comment);
    }

    @Override
    public void deleteByAuthor(Long eventId, Long authorId, Long id) {
        checkEvent(eventId);
        checkUser(authorId);

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Comment not found")
        );

        if (!comment.getUser().getId().equals(authorId)) {
            throw new LogicErrorException("User don't own this comment");
        }

        if (comment.getCreated().isBefore(LocalDateTime.now().minusHours(AUTHOR_DELETE_HOURS_LIMIT))) {
            throw new LogicErrorException("You can't delete this comment");
        }

        try {
            commentRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw new DataErrorException("DB error: " + e.getMessage());
        }
    }

    @Override
    public void deleteByAdmin(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new NotFoundException("Comment not found");
        }

        try {
            commentRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw new DataErrorException("DB error: " + e.getMessage());
        }
    }

    private void checkEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new NotFoundException("Event with id=" + id + " not found");
        }
    }

    private void checkUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User with id=" + id + " not found");
        }
    }

}
