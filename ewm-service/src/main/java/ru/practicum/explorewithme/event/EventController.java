package ru.practicum.explorewithme.event;

import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.HitDto;
import ru.practicum.explorewithme.StatsClient;
import ru.practicum.explorewithme.event.dto.EventInfo;
import ru.practicum.explorewithme.event.dto.InputEventDto;
import ru.practicum.explorewithme.event.dto.OutputEventDto;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.service.StatsService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EventController {

  private final EventService eventService;
  private final StatsClient statsClient;

  private final StatsService service;

  @GetMapping("/users/{userId}/events")
  public List<OutputEventDto> getAll(@PathVariable long userId,
      @RequestParam(defaultValue = "0") int from,
      @RequestParam(defaultValue = "10") int size) {
    return eventService.getAll(userId, from, size);
  }

  @PostMapping("/users/{userId}/events")
  @ResponseStatus(HttpStatus.CREATED)
  public OutputEventDto create(@PathVariable long userId, @Valid @RequestBody InputEventDto inputEventDto) {
    return eventService.create(userId, inputEventDto);
  }

  @GetMapping("/users/{userId}/events/{eventId}")
  public OutputEventDto getById(@PathVariable long userId, @PathVariable long eventId) {
    return eventService.getById(userId, eventId);
  }

  @PatchMapping("/users/{userId}/events/{eventId}")
  public OutputEventDto update(@PathVariable long userId,
      @PathVariable long eventId,
      @RequestBody InputEventDto inputEventDto) {
    return eventService.update(userId, eventId, inputEventDto);
  }

  @GetMapping("/admin/events")
  public List<EventInfo> search(@RequestParam(required = false) List<Long> users,
                                      @RequestParam(required = false) List<String> states,
                                      @RequestParam(required = false) List<Integer> categories,
                                      @RequestParam(required = false) String rangeStart,
                                      @RequestParam(required = false) String rangeEnd,
                                      @RequestParam(defaultValue = "0") int from,
                                      @RequestParam(defaultValue = "10") int size) {
    return eventService.search(users, states, categories, rangeStart, rangeEnd, from, size);
  }

  @PatchMapping("/admin/events/{eventId}")
  public OutputEventDto adminUpdate(@PathVariable long eventId, @RequestBody InputEventDto inputEventDto) {
    return eventService.update(eventId, inputEventDto);
  }

  @GetMapping("/events")
  public List<EventInfo> getFullEventInfo(@RequestParam(required = false) String text,
                                          @RequestParam(required = false) List<Integer> categories,
                                          @RequestParam(required = false) Boolean paid,
                                          @RequestParam(required = false) String rangeStart,
                                          @RequestParam(required = false) String rangeEnd,
                                          @RequestParam(required = false) Boolean onlyAvailable,
                                          @RequestParam(required = false) String sort,
                                          @RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size,
                                          HttpServletRequest request) {
    log.info("hit start");
    statsClient.hit(new HitDto("ewm-service", request.getRequestURI(), request.getRemoteAddr(),
            LocalDateTime.now()));
    service.hit(new HitDto("ewm-service", request.getRequestURI(), request.getRemoteAddr(),
            LocalDateTime.now()));
    log.info("hit end");
    return eventService.getFullInfo(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
  }

  @GetMapping("/events/{eventId}")
  public EventInfo getFullEventInfo(@PathVariable long eventId, HttpServletRequest request) {
    statsClient.hit(new HitDto("ewm-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now()));
    return eventService.getFullInfoById(eventId);
  }
}
