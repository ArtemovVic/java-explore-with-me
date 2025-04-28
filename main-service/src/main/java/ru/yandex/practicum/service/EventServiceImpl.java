package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dao.mappers.EventMapper;
import ru.yandex.practicum.dao.model.Category;
import ru.yandex.practicum.dao.model.Event;
import ru.yandex.practicum.dao.model.Location;
import ru.yandex.practicum.dao.model.User;
import ru.yandex.practicum.dao.model.constant.AdminEventState;
import ru.yandex.practicum.dao.model.constant.EventState;
import ru.yandex.practicum.dao.model.constant.Sort;
import ru.yandex.practicum.dao.model.constant.UserEventState;
import ru.yandex.practicum.dto.admin.event.AdminSearchEventDto;
import ru.yandex.practicum.dto.admin.event.AdminUpdateEventDto;
import ru.yandex.practicum.dto.event.*;
import ru.yandex.practicum.exception.DataErrorException;
import ru.yandex.practicum.exception.InvalidArgumentException;
import ru.yandex.practicum.exception.LogicErrorException;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.repository.CategoryRepository;
import ru.yandex.practicum.repository.EventRepository;
import ru.yandex.practicum.repository.UserRepository;
import ru.yandex.practicum.service.util.EventService;
import ru.yandex.practicum.service.util.StatsService;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    public static final String EVENT_ROUTE_PATH = "/events/";

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final StatsService statsService;

    @Override
    public List<EventDto> getList(SearchEventDto searchDto) {

        boolean hasRangeStart = searchDto.getRangeStart() != null;
        boolean hasRangeEnd = searchDto.getRangeEnd() != null;

        Pageable pageable = PageRequest.of(searchDto.getFrom() / searchDto.getSize(), searchDto.getSize());
        Specification<Event> spec = Specification.where(null);

        if (searchDto.getText() != null && !searchDto.getText().isBlank()) {
            spec = spec.and((root, query, builder) ->
                    builder.or(
                            builder.like(
                                    builder.lower(root.get("annotation")),
                                    "%" + searchDto.getText().toLowerCase() + "%"
                            ),
                            builder.like(
                                    builder.lower(root.get("description")),
                                    "%" + searchDto.getText().toLowerCase() + "%"
                            )
                    )
            );
        }

        if (searchDto.getCategories() != null && !searchDto.getCategories().isEmpty()) {
            spec = spec.and((root, query, builder) ->
                    root.get("category").get("id").in(searchDto.getCategories())
            );
        }

        if (searchDto.isPaid()) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("paid"), searchDto.isPaid())
            );
        }

        if (hasRangeStart) {
            spec = spec.and(((root, query, builder) ->
                    builder.greaterThan(root.get("eventDate"), searchDto.getRangeStart())
            ));
        }

        if (hasRangeEnd) {
            spec = spec.and(((root, query, builder) ->
                    builder.lessThan(root.get("eventDate"), searchDto.getRangeEnd())
            ));
        }

        if (searchDto.isOnlyAvailable()) {
            spec = spec.and((root, query, builder) ->
                    builder.or(
                            builder.equal(root.get("participantLimit"), 0),
                            builder.greaterThan(root.get("participantLimit"), root.join("confirmedRequests"))
                    )
            );
        }

        List<Event> events = eventRepository.findAll(spec, pageable).getContent();

        if (events.isEmpty()) {
            return Collections.emptyList();
        }

        Set<String> uris = new HashSet<>();

        for (Event event : events) {
            uris.add(getUrl(event));
        }
        Map<String, Integer> views = statsService.getViewsCount(uris);

        Comparator<EventDto> comparator = searchDto.getSort().equals(Sort.EVENT_DATE)
                ? Comparator.comparing(EventDto::getEventDate)
                : Comparator.comparing(EventDto::getViews);

        return events.stream().map(event -> EventMapper.toDto(
                event,
                views.getOrDefault(getUrl(event), 0)
        )).sorted(comparator).toList();
    }

    @Override
    public List<EventDto> getListByUser(Long userId, int from, int size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id " + userId + " not found");
        }

        Pageable pageable = PageRequest.of(from / size, size);

        return prepareEventDtoList(
                eventRepository.findAllByUser_Id(userId, pageable)
        );
    }

    @Override
    public EventDto getById(Long id) {
        Event event = eventRepository.findByIdAndState(id, EventState.PUBLISHED).orElseThrow(
                () -> new NotFoundException("Event with id " + id + " not found")
        );

        String uri = getUrl(event);

        return EventMapper.toDto(
                event,
                statsService.getUniqueViewsCount(Set.of(uri)).getOrDefault(uri, 0)
        );
    }

    @Override
    public EventDto getByIdAndUser(Long id, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id " + userId + " not found");
        }

        Event event = eventRepository.findByIdAndUser_Id(id, userId).orElseThrow(
                () -> new NotFoundException("Event with id " + id + " not found")
        );

        String uri = getUrl(event);

        return EventMapper.toDto(
                event,
                statsService.getViewsCount(Set.of(uri)).getOrDefault(uri, 0)
        );
    }

    @Override
    public List<EventDto> getListForAdmin(AdminSearchEventDto searchDto) {

        Pageable pageable = PageRequest.of(searchDto.getFrom() / searchDto.getSize(), searchDto.getSize());
        Specification<Event> spec = Specification.where(null);

        if (searchDto.getUsers() != null && !searchDto.getUsers().isEmpty()) {
            spec = spec.and((root, query, builder) ->
                    root.get("user").get("id").in(searchDto.getUsers()
                    ));
        }

        if (searchDto.getStates() != null && !searchDto.getStates().isEmpty()) {
            spec = spec.and((root, query, builder) ->
                    root.get("state").in(searchDto.getStates()
                    ));
        }

        if (searchDto.getCategories() != null && !searchDto.getCategories().isEmpty()) {
            spec = spec.and((root, query, builder) ->
                    root.get("category").get("id").in(searchDto.getCategories()
                    ));
        }

        if (searchDto.getRangeStart() != null) {
            spec = spec.and(((root, query, builder) ->
                    builder.greaterThan(root.get("eventDate"), searchDto.getRangeStart())
            ));
        }

        if (searchDto.getRangeEnd() != null) {
            spec = spec.and(((root, query, builder) ->
                    builder.lessThan(root.get("eventDate"), searchDto.getRangeEnd())
            ));
        }

        return prepareEventDtoList(
                eventRepository.findAll(spec, pageable).getContent()
        );
    }

    @Override
    @Transactional
    public EventDto updateByAdmin(Long id, AdminUpdateEventDto dto) {
        Event event = eventRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Event with id " + id + " not found")
        );

        if (event.getState() == EventState.PUBLISHED || event.getState() == EventState.CANCELED) {
            throw new LogicErrorException("Event with id " + id + " already published or canceled");
        }

        updateEventFromDto(event, dto);

        if (dto.getEventDate() != null) {
            if (dto.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new InvalidArgumentException("Event date should be before 1 hour of publish date");
            }

            event.setEventDate(dto.getEventDate());
        }

        if (dto.getStateAction() == AdminEventState.PUBLISH_EVENT) {
            event.setState(EventState.PUBLISHED);
            event.setPublished(LocalDateTime.now());
        } else if (dto.getStateAction() == AdminEventState.REJECT_EVENT) {
            event.setState(EventState.CANCELED);
            event.setPublished(null);
        }

        try {
            event = eventRepository.save(event);
        } catch (DataAccessException e) {
            throw new DataErrorException("DB error: " + e.getMessage());
        }

        String uri = getUrl(event);

        return EventMapper.toDto(
                event,
                statsService.getViewsCount(Set.of(uri)).getOrDefault(uri, 0)
        );
    }

    @Override
    @Transactional
    public EventDto createByUser(Long userId, UserCreateEventDto dto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id " + userId + " not found")
        );

        Category category = categoryRepository.findById(dto.getCategory()).orElseThrow(
                () -> new NotFoundException("Category with id " + dto.getCategory() + " not found")
        );

        Event event = EventMapper.toEntity(dto, user, category);

        try {
            event = eventRepository.save(event);
        } catch (DataAccessException e) {
            throw new DataErrorException("DB error: " + e.getMessage());
        }

        return EventMapper.toDto(event, 0);
    }

    @Override
    @Transactional
    public EventDto updateByUser(Long id, Long userId, UserUpdateEventDto dto) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with id " + userId + " not found");
        }

        Event event = eventRepository.findByIdAndUser_Id(id, userId).orElseThrow(
                () -> new NotFoundException("Event with id " + id + " not found")
        );

        if (event.getState() == EventState.PUBLISHED) {
            throw new LogicErrorException("Event with id " + id + " already published");
        }

        updateEventFromDto(event, dto);

        if (dto.getEventDate() != null) {
            if (dto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new InvalidArgumentException("Event date should be before 2 hour of publish date");
            }

            event.setEventDate(dto.getEventDate());
        }

        if (dto.getStateAction() != null) {
            if (dto.getStateAction() == UserEventState.CANCEL_REVIEW) {
                event.setState(EventState.CANCELED);
                event.setPublished(null);
            } else if (dto.getStateAction() == UserEventState.SEND_TO_REVIEW) {
                event.setState(EventState.PENDING);
                event.setPublished(null);
            }
        }

        try {
            event = eventRepository.save(event);
        } catch (DataAccessException e) {
            throw new DataErrorException("DB error: " + e.getMessage());
        }

        String uri = getUrl(event);

        return EventMapper.toDto(
                event,
                statsService.getViewsCount(Set.of(uri)).getOrDefault(uri, 0)
        );
    }

    public String getUrl(Event event) {
        return EVENT_ROUTE_PATH + event.getId();
    }

    public Long getIdFromUrl(String url) {
        return Long.valueOf(url.replace(EVENT_ROUTE_PATH, ""));
    }

    private void updateEventFromDto(Event event, UpdateEventDto dto) {
        if (dto.getAnnotation() != null) {
            event.setAnnotation(dto.getAnnotation());
        }

        if (dto.getDescription() != null) {
            event.setDescription(dto.getDescription());
        }

        if (dto.getTitle() != null) {
            event.setTitle(dto.getTitle());
        }

        if (dto.getCategory() != null && dto.getCategory() > 0) {
            event.setCategory(categoryRepository.findById(dto.getCategory()).orElseThrow(
                    () -> new NotFoundException("Category with id " + dto.getCategory() + " not found")
            ));
        }

        if (dto.getPaid() != null) {
            event.setPaid(dto.getPaid());
        }

        if (dto.getRequestModeration() != null) {
            event.setRequestModeration(dto.getRequestModeration());
        }

        if (dto.getParticipantLimit() != null) {
            event.setParticipantLimit(dto.getParticipantLimit());
        }

        if (dto.getLocation() != null) {
            Location location = new Location();
            location.setLat(dto.getLocation().lat());
            location.setLon(dto.getLocation().lon());

            event.setLocation(location);
        }
    }

    private List<EventDto> prepareEventDtoList(List<Event> events) {
        if (events == null || events.isEmpty()) {
            return Collections.emptyList();
        }

        Set<String> uris = new HashSet<>();

        for (Event event : events) {
            uris.add(getUrl(event));
        }
        Map<String, Integer> views = statsService.getViewsCount(uris);

        return events.stream().map(event -> EventMapper.toDto(
                event,
                views.getOrDefault(getUrl(event), 0)
        )).toList();
    }
}
