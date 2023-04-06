package ru.practicum.explorewithme.user.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.user.UserMapper;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository uRepo;

  @Override
  @Transactional
  public UserDto create(UserDto userDto) {
    User user = uRepo.save(UserMapper.toUser(userDto));
    return UserMapper.toUserDto(user);
  }

  @Override
  public List<UserDto> getAll(List<Long> id, int from, int size) {
    Pageable pageable = PageRequest.of(from / size, size);
   // return uRepo.getUsers(id, from, size);
    log.info("Возврат списка пользователей");
    if (id.isEmpty()) {
      return uRepo.findAll().stream()
              .map(UserMapper::toUserDto)
              .collect(Collectors.toList());
    } else {
      return uRepo.findAllByIdIn(id, pageable).stream()
              .map(UserMapper::toUserDto)
              .collect(Collectors.toList());
    }
  }

  @Override
  public List<UserDto> findAllByIdIn(List<Long> ids, Integer from, Integer size) {

    Pageable pageable = PageRequest.of(from, size);

    return uRepo.findAllByIdIn(ids, pageable).stream()
            .map(UserMapper::toUserDto)
            .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void delete(long userId) {
    User user = uRepo.getReferenceById(userId);
    uRepo.deleteById(user.getId());
  }
}

