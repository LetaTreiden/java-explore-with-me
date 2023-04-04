package ru.practicum.explorewithme.user.service;

import java.util.List;
import ru.practicum.explorewithme.user.dto.UserDto;

public interface UserService {

  UserDto createUser(UserDto userDto);

  List<UserDto> getUsers(List<Long> ids, int from, int size);

  void deleteUser(long userId);
}
