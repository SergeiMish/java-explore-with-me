package ru.practicum.service.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;
import ru.practicum.model.Location;
import ru.practicum.service.dto.category.CategoryDto;
import ru.practicum.service.dto.user.UserShortDto;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private CategoryDto category;

    @Builder.Default
    private Long confirmedRequests = 0L;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdOn;

    @Size(min = 20, max = 7000)
    private String description;

    @NotBlank
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String eventDate;

    private Long id;

    @NotNull
    private UserShortDto initiator;

    @NotNull
    private Location location;

    @NotNull
    @Builder.Default
    private Boolean paid = false;

    private Integer participantLimit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String publishedOn;

    @Builder.Default
    private Boolean requestModeration = false;

    private String state;

    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

    @Builder.Default
    private Long views = 0L;
}
