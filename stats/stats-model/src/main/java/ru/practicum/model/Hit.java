package ru.practicum.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "hits")
@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String app;

    @Column(nullable = false, length = 512)
    private String uri;

    @Column(nullable = false, length = 45)
    private String ip;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

}
