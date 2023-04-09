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
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
        Pageable pageable = PageRequest.of(from, size);
        List<User> users = uRepo.findAllByIdIn(id, pageable);
        List<UserDto> userDtos = new ArrayList<>();
        for (User u : users) {
            userDtos.add(UserMapper.toUserDto(u));
        }
        return userDtos;
    }

    @Override
    public List<UserDto> getAllTwo(List<Long> id, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        log.info(pageable.toString());
        log.info("id size " + id.size());
        return UserMapper.mapToUserDto(uRepo.findAllByIdIn(id, pageable));
    }

    @Override
    @Transactional
    public void delete(long userId) throws NoSuchElementException {
        Optional<User> user = uRepo.findById(userId);
        if (user.isEmpty()) {
            throw new NoSuchElementException("User not found");
        }
        User user1 = uRepo.getReferenceById(userId);
        log.info(user1.toString());
        uRepo.deleteById(user1.getId());
    }
}
