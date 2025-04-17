package ru.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import ru.practicum.model.enums.EventState;
import ru.practicum.model.enums.RequestStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @Column(name = "event_date")
    @Future
    private LocalDateTime eventDate;

    @Embedded
    private Location location;

    @Builder.Default
    private Boolean paid = false;

    @Column(name = "participant_limit")
    @Min(0)
    @Builder.Default
    private Integer participantLimit = 0;

    @Column(name = "request_moderation")
    @Builder.Default
    private Boolean requestModeration = true;

    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User initiator;

    @Enumerated(EnumType.STRING)
    private EventState state = EventState.PENDING;

    @Column(name = "created_on")
    @Builder.Default
    private LocalDateTime createdOn = LocalDateTime.now();

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ParticipationRequest> requests = new ArrayList<>();

    @Column(name = "views")
    @Builder.Default
    private Long views = 0L;

    @Transient
    public Long getConfirmedRequests() {
        if (requests == null) return 0L;
        return requests.stream()
                .filter(r -> r.getStatus() == RequestStatus.CONFIRMED)
                .count();
    }
}