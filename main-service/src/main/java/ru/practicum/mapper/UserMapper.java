package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.model.User;
import ru.practicum.service.dto.request.NewUserRequest;
import ru.practicum.service.dto.user.UserDto;
import ru.practicum.service.dto.user.UserShortDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserShortDto toShortDto(User user);

    UserDto toUserDto(User user);

    User toUser(NewUserRequest newUserRequest);
}