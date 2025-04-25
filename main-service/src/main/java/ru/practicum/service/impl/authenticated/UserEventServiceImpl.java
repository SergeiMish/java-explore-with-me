package ru.practicum.service.impl.authenticated;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exeption.BadRequestException;
import ru.practicum.exeption.ConflictException;
import ru.practicum.exeption.NotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.ParticipationRequestMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.User;
import ru.practicum.model.enums.EventState;
import ru.practicum.model.enums.RequestStatus;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.ParticipationRequestRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.dto.event.EventFullDto;
import ru.practicum.service.dto.event.EventShortDto;
import ru.practicum.service.dto.event.NewEventDto;
import ru.practicum.service.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.service.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.service.dto.request.ParticipationRequestDto;
import ru.practicum.service.dto.request.UpdateEventUserRequest;
import ru.practicum.service.interfaces.authenticated.UserEventService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserEventServiceImpl implements UserEventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRequestRepository requestRepository;
    private final EventMapper eventMapper;
    private final ParticipationRequestMapper participationRequestMapper;

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size) {
        List<Event> events = eventRepository.findByInitiatorId(userId);
        return events.stream()
                .skip(from)
                .limit(size)
                .map(eventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("Event date must be at least 2 hours from now");
        }

        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        Event event = eventMapper.toEvent(newEventDto);
        event.setInitiator(initiator);
        event.setCategory(category);
        event.setState(EventState.PENDING);
        event.setPublishedOn(null);

        Event savedEvent = eventRepository.save(event);
        return eventMapper.toFullDto(savedEvent);
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto getUserEvent(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        return eventMapper.toFullDto(event);
    }

    @Transactional
    @Override
    public EventFullDto updateUserEvent(Long userId, Long eventId, UpdateEventUserRequest updateRequest) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Cannot modify published event");
        }

        if (updateRequest.getEventDate() != null &&
                updateRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Event date must be at least 2 hours from now");
        }

        updateEventFields(event, updateRequest);

        Event updatedEvent = eventRepository.save(event);
        return eventMapper.toFullDto(updatedEvent);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        if (!eventRepository.existsByIdAndInitiatorId(eventId, userId)) {
            throw new NotFoundException("Event not found");
        }
        return requestRepository.findByEventId(eventId).stream()
                .map(participationRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult updateRequestStatus(
            Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest) {

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        long confirmedRequests = getConfirmedRequests(eventId);
        if (event.getParticipantLimit() > 0 && confirmedRequests >= event.getParticipantLimit()
                && updateRequest.getStatus() == RequestStatus.CONFIRMED) {
            throw new ConflictException("The participant limit has been reached");
        }

        List<ParticipationRequest> requests = requestRepository.findAllById(updateRequest.getRequestIds());
        requests.forEach(request -> {
            if (request.getStatus() != RequestStatus.PENDING) {
                throw new ConflictException("Request must have status PENDING");
            }
        });

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        List<ParticipationRequestDto> confirmed = new ArrayList<>();
        List<ParticipationRequestDto> rejected = new ArrayList<>();

        if (updateRequest.getStatus() == RequestStatus.CONFIRMED) {
            long availableSlots = event.getParticipantLimit() - confirmedRequests;
            for (ParticipationRequest request : requests) {
                if (availableSlots > 0) {
                    request.setStatus(RequestStatus.CONFIRMED);
                    confirmed.add(participationRequestMapper.toDto(request));
                    availableSlots--;
                } else {
                    request.setStatus(RequestStatus.REJECTED);
                    rejected.add(participationRequestMapper.toDto(request));
                }
            }
        } else {
            requests.forEach(request -> {
                request.setStatus(RequestStatus.REJECTED);
                rejected.add(participationRequestMapper.toDto(request));
            });
        }

        requestRepository.saveAll(requests);
        result.setConfirmedRequests(confirmed);
        result.setRejectedRequests(rejected);
        return result;
    }

    private void updateEventFields(Event event, UpdateEventUserRequest updateRequest) {

        setIfNotNull(updateRequest.getAnnotation(), event::setAnnotation);
        setIfNotNull(updateRequest.getDescription(), event::setDescription);
        setIfNotNull(updateRequest.getEventDate(), event::setEventDate);
        setIfNotNull(updateRequest.getPaid(), event::setPaid);
        setIfNotNull(updateRequest.getParticipantLimit(), event::setParticipantLimit);
        setIfNotNull(updateRequest.getTitle(), event::setTitle);

        Optional.ofNullable(updateRequest.getCategory())
                .map(categoryId -> categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new NotFoundException("Category not found")))
                .ifPresent(event::setCategory);

        Optional.ofNullable(updateRequest.getStateAction())
                .ifPresent(action -> {
                    switch (action) {
                        case SEND_TO_REVIEW:
                            validateState(event, EventState.CANCELED, "Only canceled events can be sent to review");
                            event.setState(EventState.PENDING);
                            break;
                        case CANCEL_REVIEW:
                            validateState(event, EventState.PENDING, "Only pending events can be canceled");
                            event.setState(EventState.CANCELED);
                            break;
                    }
                });
    }

    private <T> void setIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    private void validateState(Event event, EventState requiredState, String errorMessage) {
        if (event.getState() != requiredState) {
            throw new ConflictException(errorMessage);
        }
    }

    private long getConfirmedRequests(Long eventId) {
        return requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
    }
}