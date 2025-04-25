package ru.practicum.service.dto.compilation;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {
    @Builder.Default
    private List<Long> events = new ArrayList<>();
    private Boolean pinned = false;

    @Size(max = 50, message = "Максимальная длина названия - 50 символов")
    private String title;
}
