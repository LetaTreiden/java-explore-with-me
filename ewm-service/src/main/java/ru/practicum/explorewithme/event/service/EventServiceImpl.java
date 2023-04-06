package ru.practicum.explorewithme.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.repository.CategoryRepository;
import ru.practicum.explorewithme.event.EventMapper;
import ru.practicum.explorewithme.event.dto.EventInfo;
import ru.practicum.explorewithme.event.dto.InputEventDto;
import ru.practicum.explorewithme.event.dto.OutputEventDto;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventStatus;
import ru.practicum.explorewithme.event.model.State;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.exceptions.ValidationException;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

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
        if (inputEventDto.getEventDate().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Неверная дата");
        }
        User user = checkUserExistence(userId);
        Category category = checkCategoryExistence(Long.valueOf(inputEventDto.getCategory()));
        log.info(inputEventDto.toString());
        Event event = (EventMapper.toEvent(inputEventDto));
        event.setInitiator(user);
        event.setCategory(category);
        event.setState(EventStatus.PENDING);
        eventRepository.save(event);
        return EventMapper.toDto(event);
    }

    @Override
    public OutputEventDto getById(long userId, long eventId) {
        Event event = checkEventExistence(eventId);
        /*if (event.getViews() != null) {
            event.setViews(event.getViews() + 1);
        } else event.setViews(1L);

         */
      //  eventRepository.save(event);

        return EventMapper.toDto(event);
    }

    @Override
    public OutputEventDto update(long userId, long eventId, InputEventDto inputEventDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException());
        log.info("event " + event.getState());
        if (inputEventDto.getState() == null) {
            //log.info(inputEventDto.getStateAction().toString());
            inputEventDto.setState(inputEventDto.getStateAction());
        }

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
        log.info(updEvent.getState().toString());
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
        log.info("update event");
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException());
        Event updEvent = EventMapper.toEvent(inputEventDto, event);

        log.info("ev " + event.getState().toString());
        if (inputEventDto.getState() == null) {
            log.info("st act " + inputEventDto.getStateAction());
            inputEventDto.setState(inputEventDto.getStateAction());
            //  log.info("state in input final " + inputEventDto.getState().toString());
        }

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
        if (inputEventDto.getState() == State.PUBLISH_EVENT) {
            newState = EventStatus.PUBLISHED;
        } else if (inputEventDto.getState() == State.CANCEL_REVIEW
                || inputEventDto.getState() == State.REJECT_EVENT
        ) {
            newState = EventStatus.CANCELED;
        } else {
            newState = EventStatus.PENDING;
        }

        log.info("new state " + newState);
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

    private User checkUserExistence(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            throw new NoSuchElementException("Пользователь, по которому запрашиваются события, не существует");
        });
        return user;
    }

    private Event checkEventExistence(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new NoSuchElementException("Событие не существует");
        });
        return event;
    }

    private Category checkCategoryExistence(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> {
            throw new NoSuchElementException("Категория не существует");
        });
        return category;
    }
}
