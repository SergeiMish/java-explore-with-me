package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.service.dto.request.ParticipationRequestDto;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ParticipationRequestMapper {

    @Mapping(target = "requester", expression = "java(request.getRequester().getId())")
    @Mapping(target = "event", expression = "java(request.getEvent().getId())")
    ParticipationRequestDto toDto(ParticipationRequest request);

    default List<ParticipationRequestDto> toDtoList(List<ParticipationRequest> requests) {
        return requests.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}

