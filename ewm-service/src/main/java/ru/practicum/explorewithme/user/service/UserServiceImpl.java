package ru.practicum.explorewithme.user.service;

import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.user.UserMapper;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {

  private final UserRepository uRepo;

  @Override
  public UserDto create(UserDto userDto) {
    User user = uRepo.save(UserMapper.toUser(userDto));
    return UserMapper.toUserDto(user);
  }

  @Override
  public List<UserDto> getAll(List<Long> id, int from, int size) {
    return uRepo.getUsers(id, from, size);
  }

  @Override
  public void delete(long userId) {
    uRepo.deleteById(userId);
  }
}
