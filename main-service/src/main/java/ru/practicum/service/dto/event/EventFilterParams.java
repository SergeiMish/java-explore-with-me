package ru.practicum.service.dto.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.practicum.model.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class EventFilterParams {
    private final List<Long> users;
    private final List<EventState> states;
    private final List<Long> categories;
    private final LocalDateTime rangeStart;
    private final LocalDateTime rangeEnd;
    private final Integer from;
    private final Integer size;
}