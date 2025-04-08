package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.HitDto;
import ru.practicum.model.Hit;

@Mapper(componentModel = "spring")
public interface HitDtoMapper {
    HitDto toDto(Hit entity);

    Hit toEntity(HitDto dto);
}
