package ru.practicum.service.impl.admin;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exeption.BadRequestException;
import ru.practicum.exeption.ConflictException;
import ru.practicum.exeption.NotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.enums.EventState;
import ru.practicum.model.enums.RequestStatus;
import ru.practicum.model.enums.StateAction;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.ParticipationRequestRepository;
import ru.practicum.service.dto.event.EventFilterParams;
import ru.practicum.service.dto.event.EventFullDto;
import ru.practicum.service.dto.request.UpdateEventAdminRequest;
import ru.practicum.service.interfaces.admin.AdminEventService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class AdminEventServiceImpl implements AdminEventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;
    private final ParticipationRequestRepository requestRepository;

    @Transactional(readOnly = true)
    @Override
    public List<EventFullDto> searchEvents(EventFilterParams eventFilterParams) {
        Specification<Event> spec = buildSpecification(eventFilterParams);
        Pageable pageable = buildPageable(eventFilterParams.getFrom(), eventFilterParams.getSize());

        List<Event> events = eventRepository.findAll(spec, pageable).getContent();

        return events.stream()
                .map(this::convertToDtoWithConfirmedRequests)
                .collect(Collectors.toList());
    }

    private Specification<Event> buildSpecification(EventFilterParams params) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.getUsers() != null && !params.getUsers().isEmpty()) {
                predicates.add(root.get("initiator").get("id").in(params.getUsers()));
            }

            if (params.getStates() != null && !params.getStates().isEmpty()) {
                predicates.add(root.get("state").in(params.getStates()));
            }

            if (params.getCategories() != null && !params.getCategories().isEmpty()) {
                predicates.add(root.get("category").get("id").in(params.getCategories()));
            }

            if (params.getRangeStart() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("eventDate"), params.getRangeStart()));
            }

            if (params.getRangeEnd() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("eventDate"), params.getRangeEnd()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable buildPageable(Integer from, Integer size) {
        if (from == null || size == null || size <= 0) {
            throw new BadRequestException("Invalid pagination parameters");
        }
        return PageRequest.of(from / size, size);
    }

    private EventFullDto convertToDtoWithConfirmedRequests(Event event) {
        EventFullDto dto = eventMapper.toFullDto(event);
        dto.setConfirmedRequests(getConfirmedRequests(event.getId()));
        return dto;
    }

    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        if (updateRequest.getStateAction() != null) {
            validateStateAction(event, updateRequest.getStateAction());
            event = eventRepository.saveAndFlush(event);
        }

        updateEventFields(event, updateRequest);
        Event updatedEvent = eventRepository.save(event);

        log.info("Updated event state: {}", updatedEvent.getState());

        return eventMapper.toFullDto(updatedEvent);
    }


    private void validateStateAction(Event event, StateAction stateAction) {
        if (stateAction == StateAction.PUBLISH_EVENT) {
            if (event.getState() != EventState.PENDING) {
                throw new ConflictException("Cannot publish the event because it's not in the right state: " + event.getState());
            }
            if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new ConflictException("Cannot publish the event because event date is too soon");
            }
            event.setState(EventState.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        } else if (stateAction == StateAction.REJECT_EVENT) {
            if (event.getState() != EventState.PENDING) {
                throw new ConflictException("Cannot reject the event because it's not pending");
            }
            event.setState(EventState.CANCELED);
        }
    }

    private void updateEventFields(Event event, UpdateEventAdminRequest updateRequest) {
        setIfNotNull(updateRequest.getAnnotation(), event::setAnnotation);
        setIfNotNull(updateRequest.getDescription(), event::setDescription);
        setIfNotNull(updateRequest.getEventDate(), date -> {
            if (date.isBefore(LocalDateTime.now())) {
                throw new BadRequestException("Event date cannot be in the past");
            }
            event.setEventDate(date);
        });
        setIfNotNull(updateRequest.getPaid(), event::setPaid);
        setIfNotNull(updateRequest.getParticipantLimit(), event::setParticipantLimit);
        setIfNotNull(updateRequest.getRequestModeration(), event::setRequestModeration);
        setIfNotNull(updateRequest.getTitle(), event::setTitle);

        Optional.ofNullable(updateRequest.getLocation()).ifPresent(event::setLocation);

        Optional.ofNullable(updateRequest.getCategory())
                .map(categoryId -> categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new NotFoundException("Category not found")))
                .ifPresent(event::setCategory);
    }

    private <T> void setIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    private Long getConfirmedRequests(Long eventId) {
        return requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
    }
}