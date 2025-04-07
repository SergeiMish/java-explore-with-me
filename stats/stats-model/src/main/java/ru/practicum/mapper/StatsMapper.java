package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.Hit;
import ru.practicum.repository.ViewStatsProjection;

@Mapper(componentModel = "spring")
public interface StatsMapper {
    Hit toHit(EndpointHitDto dto);


    @Mapping(target = "app", source = "app")
    @Mapping(target = "uri", source = "uri")
    @Mapping(target = "hits", source = "hits")
    ViewStatsDto toViewStatsDto(ViewStatsProjection projection);
}