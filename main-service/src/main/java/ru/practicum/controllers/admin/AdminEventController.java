package ru.practicum.controllers.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.enums.EventState;
import ru.practicum.service.dto.event.EventFilterParams;
import ru.practicum.service.dto.event.EventFullDto;
import ru.practicum.service.dto.request.UpdateEventAdminRequest;
import ru.practicum.service.interfaces.admin.AdminEventService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {

    private final AdminEventService adminEventService;
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping
    public List<EventFullDto> searchEvents(
            @RequestParam(required = false) Optional<List<Long>> users,
            @RequestParam(required = false) Optional<List<String>> states,
            @RequestParam(required = false) Optional<List<Long>> categories,
            @RequestParam(required = false) Optional<String> rangeStart,
            @RequestParam(required = false) Optional<String> rangeEnd,
            @RequestParam(defaultValue = "0") @Min(0) int from,
            @RequestParam(defaultValue = "10") @Positive int size) {

        EventFilterParams filterParams = new EventFilterParams(
                users.orElse(Collections.emptyList()),
                parseStates(states.orElse(Collections.emptyList())),
                categories.orElse(Collections.emptyList()),
                parseDateTime(rangeStart),
                parseDateTime(rangeEnd),
                from,
                size
        );

        return adminEventService.searchEvents(filterParams);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventAdminRequest updateRequest) {
        return adminEventService.updateEvent(eventId, updateRequest);
    }

    private List<EventState> parseStates(List<String> states) {
        return states.stream()
                .map(EventState::valueOf)
                .collect(Collectors.toList());
    }

    private LocalDateTime parseDateTime(Optional<String> dateTime) {
        return dateTime.map(dt -> LocalDateTime.parse(dt, DATE_TIME_FORMATTER))
                .orElse(null);
    }
}