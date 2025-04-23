package ru.practicum.service.interfaces.admin;

import ru.practicum.model.enums.EventState;
import ru.practicum.service.dto.event.EventFilterParams;
import ru.practicum.service.dto.event.EventFullDto;
import ru.practicum.service.dto.request.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {
    List<EventFullDto> searchEvents(EventFilterParams filterParams);

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateRequest);

}
