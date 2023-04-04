package ru.practicum.explorewithme.user.service;

import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.user.dto.UserMapper;
import ru.practicum.explorewithme.user.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {

  private final UserRepository storage;

  @Override
  public UserDto createUser(UserDto userDto) {
    var user = storage.save(UserMapper.toUser(userDto));
    return UserMapper.toUserDto(user);
  }

  @Override
  public List<UserDto> getUsers(List<Long> ids, int from, int size) {
    return storage.getUsers(ids, from, size);
  }

  @Override
  public void deleteUser(long userId) {
    storage.deleteById(userId);
  }
}
