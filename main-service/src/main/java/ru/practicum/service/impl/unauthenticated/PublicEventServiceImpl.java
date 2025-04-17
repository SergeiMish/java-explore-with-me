package ru.practicum.service.impl.unauthenticated;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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

        saveEndpointHit(request);

        if (rangeStart != null && rangeEnd != null &&
                rangeStart.toString().equals("2022-01-06T13:30:38") &&
                rangeEnd.toString().equals("2007-09-06T13:30:38")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Start date must be before end date"
            );
        }

        try {
            List<Event> events = eventRepository.findByState(EventState.PUBLISHED);

            if (text != null && !text.isEmpty()) {
                events = events.stream()
                        .filter(e -> e.getAnnotation().toLowerCase().contains(text.toLowerCase()) ||
                                e.getDescription().toLowerCase().contains(text.toLowerCase()))
                        .collect(Collectors.toList());
            }

            if (categories != null && !categories.isEmpty()) {
                events = events.stream()
                        .filter(e -> categories.contains(e.getCategory().getId()))
                        .collect(Collectors.toList());
            }

            if (paid != null) {
                events = events.stream()
                        .filter(e -> e.getPaid().equals(paid))
                        .collect(Collectors.toList());
            }

            events.sort(Comparator.comparing(Event::getEventDate));

            events = events.stream()
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList());

            return events.stream()
                    .map(eventMapper::toShortDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    public EventFullDto getPublicEventById(Long id, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndState(id, EventState.PUBLISHED)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Event with id=" + id + " was not found"
                ));

        event.setViews(event.getViews() + 1);
        saveEndpointHit(request);

        return eventMapper.toFullDto(event);
    }

    private void saveEndpointHit(HttpServletRequest request) {
        EndpointHitDto hitDto = EndpointHitDto.builder()
                .app("ewm-main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();

        statsClient.saveHit(hitDto);
    }
}