package ru.practicum.explorewithme.event.service;

import ru.practicum.explorewithme.event.dto.EventInfo;
import ru.practicum.explorewithme.event.dto.InputEventDto;
import ru.practicum.explorewithme.event.dto.OutputEventDto;
import ru.practicum.explorewithme.event.dto.UpdateEventDto;

import java.util.List;

public interface EventService {

    List<OutputEventDto> getAll(long userId, int from, int size);

    OutputEventDto create(long userId, InputEventDto inputEventDto);

    OutputEventDto getById(long userId, long eventId);

    OutputEventDto update(long userId, long eventId, UpdateEventDto inputEventDto);

    List<EventInfo> search(List<Long> users, List<String> states, List<Integer> categories, String rangeStart,
                           String rangeEnd, int from, int size);

    OutputEventDto update(long eventId, UpdateEventDto inputEventDto);

    List<EventInfo> getFullInfo(String text, List<Integer> categories, Boolean paid, String rangeStart,
                                String rangeEnd, Boolean onlyAvailable, String sort, int from, int size);

    EventInfo getFullInfoById(long eventId);
}
