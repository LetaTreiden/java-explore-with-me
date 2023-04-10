package ru.practicum.explorewithme.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.user.model.User;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

  public static UserDto toUserDto(@NonNull User user) {
    return new UserDto(
        user.getId(),
        user.getName(),
        user.getEmail()
    );
  }

  public static User toUser(@NonNull UserDto userDto) {
    return new User(
        userDto.getId(),
        userDto.getName(),
        userDto.getEmail()
    );
  }

  public static List<UserDto> mapToUserDto(Iterable<User> users) {
    List<UserDto> dtos = new ArrayList<>();
    for (User user : users) {
      dtos.add(toUserDto(user));
    }
    return dtos;
  }
}
