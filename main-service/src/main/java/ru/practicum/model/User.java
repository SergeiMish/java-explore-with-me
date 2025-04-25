package ru.practicum.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 6, max = 254)
    @Email
    private String email;
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "initiator", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Event> organizedEvents = new ArrayList<>();

    @OneToMany(mappedBy = "requester", fetch = FetchType.LAZY)
    @Builder.Default
    private List<ParticipationRequest> requests = new ArrayList<>();
}
