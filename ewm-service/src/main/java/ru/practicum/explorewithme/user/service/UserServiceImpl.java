package ru.practicum.explorewithme.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.user.UserMapper;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

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
        log.info("create");
        User user = uRepo.save(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAll(List<Long> id, int from, int size) {
        log.info("get all");
        Pageable pageable = PageRequest.of(from, size);
        log.info(pageable.toString());
        if (id == null) {
            id = List.of();
        }
        return UserMapper.mapToUserDto(uRepo.findAllByIdIn(id, pageable));
    }

    @Override
    @Transactional
    public void delete(long userId) {
        log.info("delete");
        User user = uRepo.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found"));
        log.info(user.toString());
        uRepo.deleteById(user.getId());
    }
}
