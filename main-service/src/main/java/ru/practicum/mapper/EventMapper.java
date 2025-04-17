package ru.practicum.mapper;

import org.mapstruct.*;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.service.dto.event.EventFullDto;
import ru.practicum.service.dto.event.EventShortDto;
import ru.practicum.service.dto.event.NewEventDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {CategoryMapper.class, UserMapper.class})
public interface EventMapper {

    default Category mapCategory(Long categoryId) {
        if (categoryId == null) return null;
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }

    @Mapping(target = "eventDate", source = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "confirmedRequests", expression = "java(event.getConfirmedRequests())")
    @Mapping(target = "views", source = "views")
    EventShortDto toShortDto(Event event);

    @Mapping(target = "eventDate", source = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "createdOn", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "publishedOn", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "confirmedRequests", expression = "java(event.getConfirmedRequests())")
    @Mapping(target = "views", source = "views")
    @Mapping(target = "state", expression = "java(event.getState().name())")
    EventFullDto toFullDto(Event event);

    @Mapping(target = "eventDate", source = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "state", constant = "PENDING")
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "requests", ignore = true)
    @Mapping(target = "views", ignore = true)
    Event toEvent(NewEventDto dto);

    @AfterMapping
    default void setAdditionalFields(@MappingTarget Event event) {
        if (event.getPaid() == null) {
            event.setPaid(false);
        }
        if (event.getParticipantLimit() == null) {
            event.setParticipantLimit(0);
        }
        if (event.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }
    }
}