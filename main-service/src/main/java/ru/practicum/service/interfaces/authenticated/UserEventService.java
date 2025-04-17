package ru.practicum.service.interfaces.authenticated;

import ru.practicum.service.dto.event.EventFullDto;
import ru.practicum.service.dto.event.EventShortDto;
import ru.practicum.service.dto.event.NewEventDto;
import ru.practicum.service.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.service.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.service.dto.request.ParticipationRequestDto;
import ru.practicum.service.dto.request.UpdateEventUserRequest;

import java.util.List;

public interface UserEventService {
    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getUserEvent(Long userId, Long eventId);

    EventFullDto updateUserEvent(Long userId, Long eventId, UpdateEventUserRequest updateRequest);

    List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId);

    List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size);

    EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest);
}
