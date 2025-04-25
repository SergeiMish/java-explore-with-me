package ru.practicum.service.interfaces.authenticated;

import ru.practicum.service.dto.request.ParticipationRequestDto;

import java.util.List;

public interface UserRequestService {
    List<ParticipationRequestDto> getUserRequests(Long userId);

    ParticipationRequestDto createRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
