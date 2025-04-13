package ru.practicum.controllers.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.dto.event.EventFullDto;
import ru.practicum.service.dto.event.UpdateEventAdminRequest;
import ru.practicum.service.interfaces.admin.AdminEventService;

import java.time.LocalDateTime;
import java.util.List;

//@RestController
//@RequestMapping("/admin/events")
//@RequiredArgsConstructor
//public class AdminEventController {
//
//    private final AdminEventService adminEventService;
//
//    @GetMapping
//    public ResponseEntity<List<EventFullDto>> searchEvents(
//            @RequestParam(required = false) List<Long> users,
//            @RequestParam(required = false) List<String> states,
//            @RequestParam(required = false) List<Long> categories,
//            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
//            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
//            @RequestParam(defaultValue = "0") int from,
//            @RequestParam(defaultValue = "10") int size) {
//
//        List<EventFullDto> events = adminEventService.searchEvents(
//                users, states, categories, rangeStart, rangeEnd, from, size);
//
//        return ResponseEntity.ok(events);
//    }
//
//    @PatchMapping("/{eventId}")
//    public ResponseEntity<EventFullDto> updateEvent(
//            @PathVariable Long eventId,
//            @Valid @RequestBody UpdateEventAdminRequest updateRequest) {
//
//        EventFullDto updatedEvent = adminEventService.updateEvent(eventId, updateRequest);
//        return ResponseEntity.ok(updatedEvent);
//    }
//}