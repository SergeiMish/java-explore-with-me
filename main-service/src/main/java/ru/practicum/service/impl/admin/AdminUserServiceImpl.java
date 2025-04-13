package ru.practicum.service.impl.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exeption.ConflictException;
import ru.practicum.exeption.NotFoundException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.dto.request.NewUserRequest;
import ru.practicum.service.dto.user.UserDto;
import ru.practicum.service.interfaces.admin.AdminUserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

        private final UserRepository userRepository;
        private final UserMapper userMapper;

        @Override
        public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
            List<User> users;

            if (ids == null || ids.isEmpty()) {
                users = userRepository.findAll();
            } else {
                users = userRepository.findAllById(ids);
            }

            return users.stream()
                    .skip(from)
                    .limit(size)
                    .map(userMapper::toUserDto)
                    .collect(Collectors.toList());
        }

        @Override
        @Transactional
        public UserDto createUser(NewUserRequest userRequest) {
            if (userRepository.existsByEmail(userRequest.getEmail())) {
                throw new ConflictException("Email уже существует");
            }

            User user = userMapper.toUser(userRequest);
            User savedUser = userRepository.save(user);
            return userMapper.toUserDto(savedUser);
        }

        @Override
        @Transactional
        public void deleteUser(Long userId) {
            if (!userRepository.existsById(userId)) {
                throw new NotFoundException("Пользователь с id=" + userId + " не найден");
            }
            userRepository.deleteById(userId);
        }
    }
