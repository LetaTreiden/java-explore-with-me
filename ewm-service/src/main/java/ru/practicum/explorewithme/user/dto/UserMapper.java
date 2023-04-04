package ru.practicum.explorewithme.user.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.practicum.explorewithme.user.model.User;

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
}
