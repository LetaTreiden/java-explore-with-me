package ru.practicum.explorewithme.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.HitDto;
import ru.practicum.explorewithme.StatsClient;
import ru.practicum.explorewithme.event.dto.EventInfo;
import ru.practicum.explorewithme.event.dto.InputEventDto;
import ru.practicum.explorewithme.event.dto.OutputEventDto;
import ru.practicum.explorewithme.event.dto.UpdateEventDto;
import ru.practicum.explorewithme.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final EventService eventService;
    private final StatsClient statsClient;

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
                                 @RequestBody UpdateEventDto eventDto) {
        return eventService.update(userId, eventId, eventDto);
    }

    @GetMapping("/admin/events")
    public List<EventInfo> search(@RequestParam(required = false) List<Long> users,
                                  @RequestParam(required = false) List<String> states,
                                  @RequestParam(required = false) List<Integer> categories,
                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime
                                              rangeStart,
                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        return eventService.search(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/admin/events/{eventId}")
    public OutputEventDto adminUpdate(@PathVariable long eventId, @RequestBody UpdateEventDto eventDto) {
        return eventService.update(eventId, eventDto);
    }

    @GetMapping("/events")
    public List<EventInfo> getFullEventInfo(@RequestParam(required = false) String text,
                                            @RequestParam(required = false) List<Integer> categories,
                                            @RequestParam(required = false) Boolean paid,
                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                LocalDateTime rangeStart,
                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                LocalDateTime rangeEnd,
                                            @RequestParam(required = false) Boolean onlyAvailable,
                                            @RequestParam(required = false) String sort,
                                            @RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "10") int size,
                                            HttpServletRequest request) {
        log.info("hit start");
        statsClient.hit(new HitDto("ewm-service", request.getRequestURI(), request.getRemoteAddr(),
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
