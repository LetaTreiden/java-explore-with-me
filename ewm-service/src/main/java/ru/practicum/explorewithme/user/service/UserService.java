package ru.practicum.explorewithme.user.service;

import java.util.List;
import ru.practicum.explorewithme.user.dto.UserDto;

public interface UserService {

  UserDto create(UserDto userDto);

  List<UserDto> getAll(List<Long> ids, int from, int size);

  void delete(long userId);
}
