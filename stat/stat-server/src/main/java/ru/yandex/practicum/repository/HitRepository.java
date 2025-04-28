package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.dao.model.Hit;
import ru.yandex.practicum.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query(
            value = "SELECT h.app, h.uri, COUNT(DISTINCT h.ip) hits "
                    + "FROM hits as h "
                    + "WHERE h.created >= :start AND h.created <= :end "
                    + "GROUP BY h.app, h.uri "
                    + "ORDER BY hits DESC",
            nativeQuery = true
    )
    List<ViewStats> getStatsByDatesUnique(LocalDateTime start, LocalDateTime end);

    @Query(
            value = "SELECT h.app, h.uri, COUNT(h.ip) hits "
                    + "FROM hits as h "
                    + "WHERE h.created >= :start AND h.created <= :end "
                    + "GROUP BY h.app, h.uri "
                    + "ORDER BY hits DESC",
            nativeQuery = true
    )
    List<ViewStats> getStatsByDates(LocalDateTime start, LocalDateTime end);

    @Query(
            value = "SELECT h.app, h.uri, COUNT(DISTINCT h.ip) hits "
                    + "FROM hits as h "
                    + "WHERE h.created >= :start AND h.created <= :end AND h.uri IN (:uris) "
                    + "GROUP BY h.app, h.uri "
                    + "ORDER BY hits DESC",
            nativeQuery = true
    )
    List<ViewStats> getStatsByDatesAndUriUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(
            value = "SELECT h.app, h.uri, COUNT(h.ip) hits "
                    + "FROM hits as h "
                    + "WHERE h.created >= :start AND h.created <= :end AND h.uri IN (:uris) "
                    + "GROUP BY h.app, h.uri "
                    + "ORDER BY hits DESC",
            nativeQuery = true
    )
    List<ViewStats> getStatsByDatesAndUri(LocalDateTime start, LocalDateTime end, List<String> uris);
}
