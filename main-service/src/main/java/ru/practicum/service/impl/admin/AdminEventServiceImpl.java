package ru.practicum.service.impl.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exeption.ConflictException;
import ru.practicum.exeption.NotFoundException;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.enums.EventState;
import ru.practicum.model.enums.StateActionAdmin;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.dto.event.EventFullDto;
import ru.practicum.service.dto.event.UpdateEventAdminRequest;
import ru.practicum.service.interfaces.admin.AdminEventService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class AdminEventServiceImpl implements AdminEventService {
//
//    private final EventRepository eventRepository;
//    private final CategoryRepository categoryRepository;
//    private final EventMapper eventMapper;
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<EventFullDto> searchEvents(
//            List<Long> users,
//            List<String> states,
//            List<Long> categories,
//            LocalDateTime rangeStart,
//            LocalDateTime rangeEnd,
//            int from,
//            int size) {
//
//        Pageable pageable = PageRequest.of(from / size, size);
//
//        List<Event> events = eventRepository.findAll();
//
//        List<Event> filteredEvents = events.stream()
//                .filter(event -> users == null || users.contains(event.getInitiator().getId()))
//                .filter(event -> states == null || states.contains(event.getState().toString()))
//                .filter(event -> categories == null || categories.contains(event.getCategory().getId()))
//                .filter(event -> rangeStart == null || event.getEventDate().isAfter(rangeStart))
//                .filter(event -> rangeEnd == null || event.getEventDate().isBefore(rangeEnd))
//                .skip(from)
//                .limit(size)
//                .toList();
//
//        return filteredEvents.stream()
//                .map(eventMapper::toEventFullDto)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    @Transactional
//    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateRequest) {
//        Event event = eventRepository.findById(eventId)
//                .orElseThrow(() -> new NotFoundException("Event not found"));
//
//        if (updateRequest.getStateActionAdmin() != null) {
//            switch (updateRequest.getStateActionAdmin()) {
//                case PUBLISH_EVENT:
//                    if (event.getState() != EventState.PENDING) {
//                        throw new ConflictException("Only pending events can be published");
//                    }
//                    if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
//                        throw new ConflictException("Event date must be at least 1 hour from now");
//                    }
//                    event.setState(EventState.PUBLISHED);
//                    event.setPublishedOn(LocalDateTime.now());
//                    break;
//
//                case REJECT_EVENT:
//                    if (event.getState() == EventState.PUBLISHED) {
//                        throw new ConflictException("Published events cannot be rejected");
//                    }
//                    event.setState(EventState.CANCELED);
//                    break;
//            }
//        }
//
//        if (updateRequest.getTitle() != null) {
//            event.setTitle(updateRequest.getTitle());
//        }
//        if (updateRequest.getAnnotation() != null) {
//            event.setAnnotation(updateRequest.getAnnotation());
//        }
//        if (updateRequest.getDescription() != null) {
//            event.setDescription(updateRequest.getDescription());
//        }
//        if (updateRequest.getCategory() != null) {
//            Category category = categoryRepository.findById(updateRequest.getCategory())
//                    .orElseThrow(() -> new NotFoundException("Category not found"));
//            event.setCategory(category);
//        }
//        if (updateRequest.getEventDate() != null) {
//            event.setEventDate(updateRequest.getEventDate());
//        }
//        if (updateRequest.getLocation() != null) {
//            event.setLocation(new Location(
//                    updateRequest.getLocation().getLat(),
//                    updateRequest.getLocation().getLon()
//            ));
//        }
//        if (updateRequest.getPaid() != null) {
//            event.setPaid(updateRequest.getPaid());
//        }
//        if (updateRequest.getParticipantLimit() != null) {
//            event.setParticipantLimit(updateRequest.getParticipantLimit());
//        }
//        if (updateRequest.getRequestModeration() != null) {
//            event.setRequestModeration(updateRequest.getRequestModeration());
//        }
//
//        Event updatedEvent = eventRepository.save(event);
//        return eventMapper.toEventFullDto(updatedEvent);
//    }
//}