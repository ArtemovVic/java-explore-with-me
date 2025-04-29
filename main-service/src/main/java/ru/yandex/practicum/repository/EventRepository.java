package ru.yandex.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.yandex.practicum.dao.model.Event;
import ru.yandex.practicum.dao.model.constant.EventState;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    Optional<Event> findByIdAndState(Long id, EventState eventState);

    List<Event> findAllByUser_Id(Long id, Pageable pageable);

    Optional<Event> findByIdAndUser_Id(Long userId, Long id);

    boolean existsByIdAndUser_Id(Long id, Long userId);

    Set<Event> findAllByIdIn(Set<Long> events);

    boolean existsByCategory_Id(Long categoryId);
}
