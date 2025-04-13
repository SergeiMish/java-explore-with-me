package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.service.dto.request.NewUserRequest;
import ru.practicum.service.dto.user.UserDto;
import ru.practicum.service.dto.user.UserShortDto;
import ru.practicum.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserShortDto toShortDto(User user);
    UserDto toUserDto(User user);
    User toUser(NewUserRequest newUserRequest);
}