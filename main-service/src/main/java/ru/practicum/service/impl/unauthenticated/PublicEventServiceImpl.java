package ru.practicum.service.impl.unauthenticated;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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

        // Валидация дат
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Start date must be before end date"
            );
        }

        // Установка дефолтных значений
        LocalDateTime start = rangeStart != null ? rangeStart : LocalDateTime.now();
        LocalDateTime end = rangeEnd;

        // Построение спецификации
        Specification<Event> spec = Specification.where(
                (root, query, cb) -> cb.equal(root.get("state"), EventState.PUBLISHED)
        );

        // Текстовый поиск (если передан)
        if (text != null && !text.isBlank()) {
            String searchText = "%" + text.toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("annotation")), searchText),
                    cb.like(cb.lower(root.get("description")), searchText)
            ));
        }

        // Фильтр по категориям (если передан)
        if (categories != null && !categories.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("category").get("id").in(categories));
        }

        // Фильтр по платности (если передан)
        if (paid != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("paid"), paid));
        }

        // Фильтр по дате начала
        spec = spec.and((root, query, cb) -> cb.greaterThan(root.get("eventDate"), start));

        // Фильтр по дате окончания (если передан)
        if (end != null) {
            spec = spec.and((root, query, cb) -> cb.lessThan(root.get("eventDate"), end));
        }

        // Фильтр по доступности (если включен)
        if (Boolean.TRUE.equals(onlyAvailable)) {
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.equal(root.get("participantLimit"), 0),
                    cb.greaterThan(root.get("participantLimit"), root.get("confirmedRequests"))
            ));
        }

        // Создание pageable для пагинации
        Pageable pageable = PageRequest.of(from / size, size);

        // Получение событий
        List<Event> events = eventRepository.findAll(spec, pageable).getContent();

        // Сортировка
        if ("VIEWS".equals(sort)) {
            events.sort((e1, e2) -> e2.getViews().compareTo(e1.getViews()));
        } else {
            // Сортировка по дате по умолчанию
            events.sort((e1, e2) -> e1.getEventDate().compareTo(e2.getEventDate()));
        }

        // Сохранение статистики
        saveEndpointHit(request);

        return events.stream()
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getPublicEventById(Long id, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndState(id, EventState.PUBLISHED)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Event with id=" + id + " was not found"
                ));

        // Сохранение статистики
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