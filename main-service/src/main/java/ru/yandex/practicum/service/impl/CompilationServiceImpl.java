package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dao.mappers.CompilationMapper;
import ru.yandex.practicum.dao.model.Compilation;
import ru.yandex.practicum.dao.model.Event;
import ru.yandex.practicum.dto.admin.compilation.AdminCreateCompilationDto;
import ru.yandex.practicum.dto.admin.compilation.AdminUpdateCompilationDto;
import ru.yandex.practicum.dto.compilation.CompilationDto;
import ru.yandex.practicum.exception.DataErrorException;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.repository.CompilationRepository;
import ru.yandex.practicum.repository.EventRepository;
import ru.yandex.practicum.service.CompilationService;
import ru.yandex.practicum.service.EventService;
import ru.yandex.practicum.service.StatsService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final EventService eventService;
    private final StatsService statsService;

    @Override
    public List<CompilationDto> getList(boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);

        List<Compilation> compilations = pinned
                ? compilationRepository.findAllByPinned(true, pageable)
                : compilationRepository.findAll(pageable).getContent();

        Map<Long, Integer> views = getViews(compilations);

        return compilations
                .stream()
                .map(el -> CompilationMapper.toDto(el, views))
                .toList();
    }

    @Override
    public CompilationDto getById(Long id) {
        Compilation compilation = compilationRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Compilation not found")
        );

        return CompilationMapper.toDto(
                compilation,
                getViews(Collections.singletonList(compilation))
        );
    }

    @Override
    @Transactional
    public CompilationDto createByAdmin(AdminCreateCompilationDto dto) {
        Compilation compilation = new Compilation();
        compilation.setPinned(dto.isPinned());
        compilation.setName(dto.getTitle());

        setEvents(compilation, dto.getEvents());

        try {
            compilation = compilationRepository.save(compilation);
        } catch (DataAccessException e) {
            throw new DataErrorException("DB error: " + e.getMessage());
        }

        return CompilationMapper.toDto(
                compilation,
                getViews(Collections.singletonList(compilation))
        );
    }

    @Override
    @Transactional
    public void deleteByAdmin(Long id) {
        if (!compilationRepository.existsById(id)) {
            throw new NotFoundException("Compilation not found");
        }

        try {
            compilationRepository.deleteById(id);
        } catch (DataAccessException e) {
            throw new DataErrorException("DB error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public CompilationDto updateByAdmin(Long id, AdminUpdateCompilationDto dto) {
        Compilation compilation = compilationRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Compilation not found")
        );

        if (dto.getTitle() != null) {
            compilation.setName(dto.getTitle());
        }

        if (dto.getPinned() != null) {
            compilation.setPinned(dto.getPinned());
        }

        setEvents(compilation, dto.getEvents());

        try {
            compilation = compilationRepository.save(compilation);
        } catch (DataAccessException e) {
            throw new DataErrorException("DB error: " + e.getMessage());
        }

        return CompilationMapper.toDto(
                compilation,
                getViews(Collections.singletonList(compilation))
        );
    }

    private Map<Long, Integer> getViews(List<Compilation> compilations) {

        if (compilations == null || compilations.isEmpty()) {
            return Collections.emptyMap();
        }

        Set<String> uris = new HashSet<>();

        for (Compilation c : compilations) {
            for (Event event : c.getEvents()) {
                uris.add(eventService.getUrl(event));
            }
        }

        if (uris.isEmpty()) {
            return Collections.emptyMap();
        }

        return statsService.getViewsCount(uris)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> eventService.getIdFromUrl(e.getKey()),
                        Map.Entry::getValue
                ));
    }

    private void setEvents(Compilation compilation, Set<Long> eventIds) {
        if (eventIds == null || eventIds.isEmpty()) {
            return;
        }

        Set<Event> events = eventRepository.findAllByIdIn(eventIds);

        if (events.isEmpty() || events.size() != eventIds.size()) {
            throw new NotFoundException("Events not found");
        }

        compilation.setEvents(events);
    }
}
