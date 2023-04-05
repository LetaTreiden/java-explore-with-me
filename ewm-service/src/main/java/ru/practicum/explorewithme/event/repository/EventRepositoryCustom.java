package ru.practicum.explorewithme.event.repository;

import java.time.LocalDateTime;
import java.util.List;

import ru.practicum.explorewithme.event.dto.State;
import ru.practicum.explorewithme.event.model.Event;

public interface EventRepositoryCustom {

  List<Event> searchEvents(List<Long> users, List<String> states, List<Integer> categories,
                           String rangeStart, String rangeEnd, int from, int size);

  List<Event> searchEvents(String text, List<Integer> categories, Boolean paid, String rangeStart,
                           String rangeEnd, Boolean onlyAvailable, String sort, int from, int size);

}
