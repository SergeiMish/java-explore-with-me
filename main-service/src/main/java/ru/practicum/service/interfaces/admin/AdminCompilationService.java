package ru.practicum.service.interfaces.admin;

import ru.practicum.service.dto.compilation.CompilationDto;
import ru.practicum.service.dto.compilation.NewCompilationDto;
import ru.practicum.service.dto.request.UpdateCompilationRequest;

public interface AdminCompilationService {
    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateRequest);
}
