package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHitDto {
    private Long id;

    @NotBlank(message = "App name cannot be blank")
    private String app;

    @NotBlank(message = "URI cannot be blank")
    private String uri;

    @NotBlank(message = "IP cannot be blank")
    @Pattern(regexp = "^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$",
            message = "Invalid IP address format")
    private String ip;

    @NotNull(message = "Timestamp cannot be null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}