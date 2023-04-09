package ru.practicum.explorewithme.event.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.HitDto;
import ru.practicum.explorewithme.StatsClient;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.repository.CategoryRepository;
import ru.practicum.explorewithme.event.EventMapper;
import ru.practicum.explorewithme.event.dto.EventInfo;
import ru.practicum.explorewithme.event.dto.InputEventDto;
import ru.practicum.explorewithme.event.dto.OutputEventDto;
import ru.practicum.explorewithme.event.dto.UpdateEventDto;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventStatus;
import ru.practicum.explorewithme.event.model.State;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.exceptions.ValidationException;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final LocalDateTime max = LocalDateTime.of(3023, 9, 19, 14, 5);

    private final LocalDateTime min = LocalDateTime.of(1023, 9, 19, 14, 5);

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<OutputEventDto> getAll(long userId, int from, int size) {
        log.info("get all");
        Sort sort = Sort.by("createdOn").descending();
        PageRequest pageable = PageRequest.of(from / size, size, sort);
        return eventRepository.findAllByInitiatorId(userId, pageable).stream()
                .map(EventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OutputEventDto create(long userId, InputEventDto inputEventDto) {
        log.info("create");
        if (inputEventDto.getEventDate().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Wrong date");
        }
        User user = checkUserExistence(userId);
        Category category = checkCategoryExistence(Long.valueOf(inputEventDto.getCategory()));
        Event event = (EventMapper.toEvent(inputEventDto));
        event.setInitiator(user);
        event.setCategory(category);
        event.setState(EventStatus.PENDING);
        eventRepository.save(event);
        return EventMapper.toDto(event);
    }

    @Override
    public OutputEventDto getById(long userId, long eventId) {
        log.info("get by id");
        Event event = checkEventExistence(eventId);
        return EventMapper.toDto(event);
    }

    @Override
    public OutputEventDto update(long userId, long eventId, UpdateEventDto dto) {
        log.info("update");
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException());
        if (dto.getState() == null) {
            dto.setState(dto.getStateAction());
        }

        if (event.getInitiator().getId() != userId) {
            throw new IllegalStateException("You don't have such rights");
        }

        if (event.getState() != EventStatus.PENDING && event.getState() != EventStatus.CANCELED) {
            throw new ValidationException("Cannot update event with status " + event.getState());
        }

        if (dto.getEventDate() != null
                && dto.getEventDate().isBefore(LocalDateTime.now().minusHours(2))) {
            throw new ValidationException("Something wrong with date");
        }

        Event updEvent = EventMapper.toEvent(dto, event);
        eventRepository.save(updEvent);
        log.info(updEvent.getState().toString());
        return EventMapper.toDto(updEvent);
    }

    @Override
    public List<EventInfo> search(List<Long> users, List<String> states, List<Integer> categories,
                                  LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        log.info("search");
        return eventRepository.searchEvents(users, states, categories, rangeStart, rangeEnd, from, size).stream()
                .map(EventMapper::toFullDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OutputEventDto update(long eventId, UpdateEventDto eventDto) {
        log.info("update event");
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException());
        Event updEvent = EventMapper.toEvent(eventDto, event);

        if (eventDto.getState() == null) {
            eventDto.setState(eventDto.getStateAction());
        }

        if ((eventDto.getState() == State.PUBLISH_EVENT
                || eventDto.getState() == State.REJECT_EVENT)
                && (event.getState() == EventStatus.PUBLISHED || event.getState() == EventStatus.CANCELED)) {
            throw new ValidationException("Cannot update event with status " + event.getState());
        }

        if (eventDto.getEventDate() != null
                && eventDto.getEventDate().isBefore(LocalDateTime.now().minusHours(2))) {
            throw new ValidationException("Something wrong with date");
        }

        EventStatus newState;
        if (eventDto.getState() == State.PUBLISH_EVENT) {
            newState = EventStatus.PUBLISHED;
        } else if (eventDto.getState() == State.CANCEL_REVIEW
                || eventDto.getState() == State.REJECT_EVENT
        ) {
            newState = EventStatus.CANCELED;
        } else {
            newState = EventStatus.PENDING;
        }

        updEvent.setState(newState);
        if (newState == EventStatus.PUBLISHED) updEvent.setPublishedOn(LocalDateTime.now());
        else updEvent.setPublishedOn(null);

        eventRepository.save(updEvent);
        log.info("state {}", updEvent.getState().toString());
        return EventMapper.toDto(updEvent);
    }

    @Override
    public List<EventInfo> getFullInfo(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                       LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, int from, int size) {
        log.info("start to search");
        return eventRepository.searchEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size)
                .stream()
                .map(EventMapper::toFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventInfo getFullInfoById(long eventId, StatsClient statsClient) {
        log.info("get full information");
        EventInfo info = EventMapper.toFullDto(eventRepository.findById(eventId).orElseThrow(()
                -> new NoSuchElementException()));
        int requests = 0;
        List<HitDto> viewsStats = viewsStats((int) eventId, statsClient);
        for (int i = 0, viewsStatsSize = viewsStats.size(); i < viewsStatsSize; i++) {
            requests++;
        }
        info.setConfirmedRequests(requests);
        return info;
    }

    private List<HitDto> viewsStats(int id, StatsClient statsClient) {
        String[] uris = new String[1];
        uris[0] = "/events/" + id;
        ResponseEntity<Object> hits = statsClient.getHits(min.format(formatter), max.format(formatter), uris,
                true);
        List<HitDto> hitList = new ObjectMapper().convertValue(hits.getBody(), new TypeReference<>() {
        });
        return hitList;
    }


    private User checkUserExistence(Long id) {
        return userRepository.findById(id).orElseThrow(() -> {
            throw new NoSuchElementException("Пользователь, по которому запрашиваются события, не существует");
        });
    }

    private Event checkEventExistence(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> {
            throw new NoSuchElementException("Событие не существует");
        });
    }

    private Category checkCategoryExistence(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> {
            throw new NoSuchElementException("Категория не существует");
        });
    }
}
