package ru.practicum.explorewithme.event.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.event.EventMapper;
import ru.practicum.explorewithme.event.dto.EventStatus;
import ru.practicum.explorewithme.event.dto.EventInfo;
import ru.practicum.explorewithme.event.dto.InputEventDto;
import ru.practicum.explorewithme.event.dto.OutputEventDto;
import ru.practicum.explorewithme.event.dto.State;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.exceptions.ValidationException;
import ru.practicum.explorewithme.user.model.User;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

  private final EventRepository eventRepository;

  @Override
  public List<OutputEventDto> getAll(long userId, int from, int size) {
    Sort sort = Sort.by("createdOn").descending();
    PageRequest pageable = PageRequest.of(from / size, size, sort);
    return eventRepository.findAllByInitiatorId(userId, pageable).stream()
        .map(EventMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public OutputEventDto create(long userId, InputEventDto inputEventDto) {
    if (inputEventDto.getEventDate() != null
        && inputEventDto.getEventDate().isBefore(LocalDateTime.now().minusHours(2))) {
      throw new ValidationException("Something wrong with date");
    }

    Event event = EventMapper.toEvent(inputEventDto);
    event.setInitiator(new User(userId));
    event.setCategory(new Category(Long.valueOf(inputEventDto.getCategory())));
    event.setState(EventStatus.PENDING);
    eventRepository.save(event);
    return EventMapper.toDto(event);
  }

  @Override
  public OutputEventDto getById(long userId, long eventId) {
    Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException());
    event.setViews(event.getViews() != null ? event.getViews() + 1 : 1);
    eventRepository.save(event);

    return EventMapper.toDto(event);
  }

  @Override
  @Transactional
  public OutputEventDto update(long userId, long eventId, InputEventDto inputEventDto) {
    Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException());

    if (event.getInitiator().getId() != userId) {
      throw new IllegalStateException("You don't have such rights");
    }

    if (event.getState() != EventStatus.PENDING && event.getState() != EventStatus.CANCELED) {
      throw new ValidationException("Cannot update event with status " + event.getState());
    }

    if (inputEventDto.getEventDate() != null
        && inputEventDto.getEventDate().isBefore(LocalDateTime.now().minusHours(2))) {
      throw new ValidationException("Something wrong with date");
    }

    Event updEvent = EventMapper.toEvent(inputEventDto, event);
    eventRepository.save(updEvent);
    return EventMapper.toDto(updEvent);
  }

  @Override
  public List<EventInfo> search(List<Long> users, List<String> states, List<Integer> categories,
                                String rangeStart, String rangeEnd, int from, int size) {
    return eventRepository.searchEvents(users, states, categories, rangeStart, rangeEnd, from, size).stream()
        .map(EventMapper::toFullDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public OutputEventDto update(long eventId, InputEventDto inputEventDto) {
    Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException());
    Event updEvent = EventMapper.toEvent(inputEventDto, event);

    if ((inputEventDto.getState() == State.PUBLISH_EVENT
        || inputEventDto.getState() == State.REJECT_EVENT)
        && (event.getState() == EventStatus.PUBLISHED || event.getState() == EventStatus.CANCELED)) {
      throw new ValidationException("Cannot update event with status " + event.getState());
    }

    if (inputEventDto.getEventDate() != null
        && inputEventDto.getEventDate().isBefore(LocalDateTime.now().minusHours(2))) {
      throw new ValidationException("Something wrong with date");
    }

    EventStatus newState;
    if (inputEventDto.getState() == State.PUBLISH_EVENT) newState = EventStatus.PUBLISHED;
    else if (inputEventDto.getState() == State.CANCEL_REVIEW) newState = EventStatus.CANCELED;
    else newState = EventStatus.PENDING;
    updEvent.setState(newState);
    if (newState == EventStatus.PUBLISHED) updEvent.setPublishedOn(LocalDateTime.now());
    else updEvent.setPublishedOn(null);

    eventRepository.save(updEvent);
    log.info(updEvent.getState().toString());
    return EventMapper.toDto(updEvent);
  }

  @Override
  public List<EventInfo> getFullInfo(String text, List<Integer> categories, Boolean paid, String rangeStart,
                                     String rangeEnd, Boolean onlyAvailable, String sort, int from, int size) {
    return eventRepository.searchEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size)
        .stream()
        .map(EventMapper::toFullDto)
        .collect(Collectors.toList());
  }

  @Override
  public EventInfo getFullInfoById(long eventId) {
    Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException());
    if (event.getViews() != null) event.setViews(event.getViews() + 1);
    else event.setViews(1L);
    eventRepository.save(event);

    return EventMapper.toFullDto(event);
  }
}
