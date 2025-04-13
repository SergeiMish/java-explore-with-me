package ru.practicum.service.dto.compilation;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {
    @NotEmpty
    private List<Long> events = new ArrayList<>();
    private Boolean pinned = false;

    @Length(min = 1, max = 50)
    private String title;
}
