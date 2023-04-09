package ru.practicum.explorewithme.event.service;

import ru.practicum.explorewithme.StatsClient;
import ru.practicum.explorewithme.event.dto.EventInfo;
import ru.practicum.explorewithme.event.dto.InputEventDto;
import ru.practicum.explorewithme.event.dto.OutputEventDto;
import ru.practicum.explorewithme.event.dto.UpdateEventDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<OutputEventDto> getAll(long userId, int from, int size);

    OutputEventDto create(long userId, InputEventDto inputEventDto);

    OutputEventDto getById(long userId, long eventId);

    OutputEventDto update(long userId, long eventId, UpdateEventDto inputEventDto);

    List<EventInfo> search(List<Long> users, List<String> states, List<Integer> categories, LocalDateTime rangeStart,
                           LocalDateTime rangeEnd, int from, int size);

    OutputEventDto update(long eventId, UpdateEventDto inputEventDto);

    List<EventInfo> getFullInfo(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, int from, int size);

    EventInfo getFullInfoById(long eventId, StatsClient statsClient);
}
