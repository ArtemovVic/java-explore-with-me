package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.dao.model.EventRequest;
import ru.yandex.practicum.dao.model.constant.EventRequestState;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {
    List<EventRequest> findAllByEvent_Id(Long eventId);

    Integer countByEvent_IdAndState(Long eventId, EventRequestState eventRequestState);

    @Query(
            value = "SELECT r.event_id, COUNT(r.event_id) "
                    + "FROM event_requests r "
                    + "WHERE r.event_id IN (:ids) AND r.state = :requestState "
                    + "GROUP BY r.event_id ",
            nativeQuery = true
    )
    Map<Long, Integer> countByEvent_IdInAndState(Set<Long> ids, EventRequestState requestState);


    List<EventRequest> findAllByUser_Id(Long userId);

    boolean existsByEvent_IdAndUser_Id(Long eventId, Long userId);

    Optional<EventRequest> findByIdAndUser_Id(Long id, Long userId);

    boolean existsByEvent_IdAndUser_IdAndStateIs(Long eventId, Long userId, EventRequestState eventRequestState);
}
