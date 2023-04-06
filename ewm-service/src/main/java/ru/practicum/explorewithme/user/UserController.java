package ru.practicum.explorewithme.user;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.user.service.UserService;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class UserController {

  private final UserService userService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserDto create(@Valid @RequestBody UserDto userDto) {
    return userService.create(userDto);
  }

  @GetMapping
  public List<UserDto> getAll(@RequestParam List<Long> ids,
      @RequestParam(defaultValue = "0") int from,
      @RequestParam(defaultValue = "10") int size) {
    log.info(ids.toString());
    log.info("get all by ids");
    return userService.getAllTwo(ids, from, size);
  }

  @DeleteMapping("/{userId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable long userId) throws NoSuchFieldException {
    userService.delete(userId);
  }
}
