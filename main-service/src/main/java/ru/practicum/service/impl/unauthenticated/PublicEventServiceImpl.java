package ru.practicum.service.impl.unauthenticated;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.exeption.ConflictException;
import ru.practicum.exeption.NotFoundException;
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

        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new ConflictException("Start date must be before end date");
        }

        List<Event> events = eventRepository.findPublicEvents(
                text != null ? text.toLowerCase() : null,
                categories,
                paid,
                rangeStart != null ? rangeStart : LocalDateTime.now(),
                rangeEnd,
                onlyAvailable);

        if ("EVENT_DATE".equals(sort)) {
            events.sort(Comparator.comparing(Event::getEventDate));
        } else if ("VIEWS".equals(sort)) {
            events.sort(Comparator.comparing(Event::getViews));
        }

        List<Event> paginatedEvents = events.stream()
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());

        saveEndpointHit(request);

        return paginatedEvents.stream()
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getPublicEventById(Long id, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndState(id, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Event not found"));

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
