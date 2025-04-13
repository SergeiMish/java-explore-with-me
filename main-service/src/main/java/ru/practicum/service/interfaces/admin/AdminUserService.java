package ru.practicum.service.interfaces.admin;

import ru.practicum.service.dto.request.NewUserRequest;
import ru.practicum.service.dto.user.UserDto;

import java.util.List;

public interface AdminUserService {
    UserDto createUser(NewUserRequest userRequest);
    void deleteUser(Long userId);
    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);
}
