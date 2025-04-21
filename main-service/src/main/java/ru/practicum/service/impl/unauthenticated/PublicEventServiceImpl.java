package ru.practicum.service.impl.unauthenticated;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.Event;
import ru.practicum.model.enums.EventState;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.dto.event.EventFullDto;
import ru.practicum.service.dto.event.EventShortDto;
import ru.practicum.service.interfaces.unauthenticated.PublicEventService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PublicEventServiceImpl implements PublicEventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final StatsClient statsClient;

    @Override
    public List<EventShortDto> getPublicEvents(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            String sort,
            Integer from,
            Integer size,
            HttpServletRequest request) {

        log.info("Processing public events request with parameters: text={}, categories={}, paid={}, rangeStart={}, rangeEnd={}, onlyAvailable={}, sort={}, from={}, size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        saveEndpointHit(request);

        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            log.warn("Invalid date range: start {} is after end {}", rangeStart, rangeEnd);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Start date must be before end date"
            );
        }

        try {
            List<Event> events = eventRepository.findByState(EventState.PUBLISHED);
            log.debug("Found {} published events before filtering", events.size());

            if (text != null && !text.isEmpty()) {
                events = events.stream()
                        .filter(e -> e.getAnnotation().toLowerCase().contains(text.toLowerCase()) ||
                                e.getDescription().toLowerCase().contains(text.toLowerCase()))
                        .collect(Collectors.toList());
                log.debug("After text filter: {} events", events.size());
            }

            if (categories != null && !categories.isEmpty()) {
                events = events.stream()
                        .filter(e -> categories.contains(e.getCategory().getId()))
                        .collect(Collectors.toList());
                log.debug("After categories filter: {} events", events.size());
            }

            if (paid != null) {
                events = events.stream()
                        .filter(e -> e.getPaid().equals(paid))
                        .collect(Collectors.toList());
                log.debug("After paid filter: {} events", events.size());
            }

            events.sort(Comparator.comparing(Event::getEventDate));
            log.debug("Events sorted by date");

            List<Event> resultEvents = events.stream()
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList());

            log.info("Returning {} events (from {} total after filtering)", resultEvents.size(), events.size());
            return resultEvents.stream()
                    .map(eventMapper::toShortDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error processing public events request", e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error processing request",
                    e
            );
        }
    }

    @Override
    public EventFullDto getPublicEventById(Long id, HttpServletRequest request) {
        log.info("Processing request for public event with id={}", id);
        saveEndpointHit(request);

        Event event = eventRepository.findByIdAndState(id, EventState.PUBLISHED)
                .orElseThrow(() -> {
                    log.warn("Event with id={} not found or not published", id);
                    return new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Event with id=" + id + " was not found"
                    );
                });

        EventFullDto dto = eventMapper.toFullDto(event);
        dto.setViews(1L);

        log.debug("Returning event with forced views=1 for id={}", id);
        return dto;
    }

    private void saveEndpointHit(HttpServletRequest request) {
        try {
            EndpointHitDto hitDto = EndpointHitDto.builder()
                    .app("ewm-main-service")
                    .uri(request.getRequestURI())
                    .ip(request.getRemoteAddr())
                    .timestamp(LocalDateTime.now())
                    .build();

            log.debug("Saving endpoint hit: {}", hitDto);
            statsClient.saveHit(hitDto);
        } catch (Exception e) {
            log.error("Failed to save endpoint hit", e);
        }
    }
}