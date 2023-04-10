package ru.practicum.explorewithme.user.service;

import ru.practicum.explorewithme.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto create(UserDto userDto);

    List<UserDto> getAll(List<Long> id, int from, int size);

    void delete(long userId);
}
