package ru.practicum.service.interfaces.admin;

import ru.practicum.service.dto.event.EventFullDto;
import ru.practicum.service.dto.event.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {
    List<EventFullDto> searchEvents(List<Long> users,
                                    List<String> states,
                                    List<Long> categories,
                                    LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd,
                                    int from,
                                    int size);

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateRequest);
}
