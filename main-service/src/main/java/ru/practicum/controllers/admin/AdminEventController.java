package ru.practicum.controllers.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.enums.EventState;
import ru.practicum.service.dto.event.EventFullDto;
import ru.practicum.service.dto.request.UpdateEventAdminRequest;
import ru.practicum.service.interfaces.admin.AdminEventService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {

    private final AdminEventService adminEventService;

    @GetMapping
    public List<EventFullDto> searchEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {

        return adminEventService.searchEvents(
                users,
                parseStates(states),
                categories,
                parseDateTime(rangeStart),
                parseDateTime(rangeEnd),
                from,
                size
        );
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventAdminRequest updateRequest) {

        return adminEventService.updateEvent(eventId, updateRequest);
    }

    private List<EventState> parseStates(List<String> states) {
        if (states == null) return null;
        return states.stream()
                .map(EventState::valueOf)
                .collect(Collectors.toList());
    }

    private LocalDateTime parseDateTime(String dateTime) {
        return dateTime != null ?
                LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) :
                null;
    }
}