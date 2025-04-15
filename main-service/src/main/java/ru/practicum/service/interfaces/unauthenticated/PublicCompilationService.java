package ru.practicum.service.interfaces.unauthenticated;

import ru.practicum.service.dto.compilation.CompilationDto;

import java.util.List;

public interface PublicCompilationService {

    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationById(Long compId);
}
