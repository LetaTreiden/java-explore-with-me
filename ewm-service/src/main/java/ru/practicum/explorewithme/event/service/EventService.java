package ru.practicum.explorewithme.event.service;

import java.util.List;
import ru.practicum.explorewithme.event.dto.FullEventInfo;
import ru.practicum.explorewithme.event.dto.InputEventDto;
import ru.practicum.explorewithme.event.dto.OutputEventDto;

public interface EventService {

  List<OutputEventDto> getEvents(long userId, int from, int size);

  OutputEventDto createEvent(long userId, InputEventDto inputEventDto);

  OutputEventDto getEvent(long userId, long eventId);

  OutputEventDto updateEvent(long userId, long eventId, InputEventDto inputEventDto);

  List<FullEventInfo> searchEvents(List<Long> users, List<String> states, List<Integer> categories, String rangeStart,
      String rangeEnd, int from, int size);

  OutputEventDto updateEvent(long eventId, InputEventDto inputEventDto);

  List<FullEventInfo> getFullEventInfo(String text, List<Integer> categories, Boolean paid, String rangeStart,
      String rangeEnd, Boolean onlyAvailable, String sort, int from, int size);

  FullEventInfo getFullEventInfo(long eventId);
}
