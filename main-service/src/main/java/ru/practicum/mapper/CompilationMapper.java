package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.model.Compilation;
import ru.practicum.service.dto.compilation.CompilationDto;
import ru.practicum.service.dto.compilation.NewCompilationDto;

@Mapper(componentModel = "spring",
        uses = EventMapper.class)
public interface CompilationMapper {

    @Mapping(target = "events", source = "events")
    CompilationDto toDto(Compilation compilation);

    @Mapping(target = "events", ignore = true)
        // Обрабатывается отдельно в сервисе
    Compilation toCompilation(NewCompilationDto dto);
}