package ru.practicum.service.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import ru.practicum.model.enums.RequestStatus;

import java.util.List;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {
    @NotEmpty
    private List<Long> requestIds;
    private RequestStatus status;

}
