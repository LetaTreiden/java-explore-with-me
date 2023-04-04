package ru.practicum.explorewithme.user.repository;

import java.util.List;
import ru.practicum.explorewithme.user.dto.UserDto;

public interface UserRepositoryCustom {

  List<UserDto> getUsers(List<Long> ids, int from, int size);
}
