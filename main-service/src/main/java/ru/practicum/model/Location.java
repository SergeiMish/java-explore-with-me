package ru.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Column(nullable = false)
    private Float lat;

    @Column(nullable = false)
    private Float lon;
}
