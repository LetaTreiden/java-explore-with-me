package ru.practicum.explorewithme.event.repository;

import java.time.LocalDateTime;
import java.util.List;

import ru.practicum.explorewithme.event.model.Event;

public interface EventRepositoryCustom {

  List<Event> searchEvents(List<Long> users, List<String> states, List<Integer> categories,
                           LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

  List<Event> searchEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                           LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, int from, int size);

}
