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
import ru.practicum.explorewithme.HitStatDto;
import ru.practicum.explorewithme.StatsClient;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.repository.CategoryRepository;
import ru.practicum.explorewithme.event.EventMapper;
import ru.practicum.explorewithme.event.dto.*;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventStatus;
import ru.practicum.explorewithme.event.model.State;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.exceptions.ValidationException;
import ru.practicum.explorewithme.request.repository.RequestRepository;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final LocalDateTime max = LocalDateTime.of(3023, 9, 19, 14, 5);

    private final LocalDateTime min = LocalDateTime.of(1023, 9, 19, 14, 5);

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final StatsClient statsClient;

    static final String URI = "/events/";

    static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final StatsClient client;

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
    @Transactional
    public OutputEventDto update(long userId, long eventId, UpdateEventUserDto dto) {
        log.info("update event user");
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException());
        if (dto.getStateAction() == null) {
            dto.setStateAction(dto.getStateAction());
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
        List<Event> events = eventRepository.searchEvents(users, states, categories, rangeStart, rangeEnd, from, size);
        if (events.isEmpty()) {
            return List.of();
        }
        Map<Long, Long> hits = get(events);
        List<Long> eventIds = new ArrayList<>();
        for (Event event: events) {
            eventIds.add(event.getId());
        }
        Map<Long, Integer> confRequests = requestRepository.getRequestsEventsConfirmed(eventIds);
        List<EventInfo> result = new ArrayList<>();
        for (Event event: events) {
            EventInfo info = EventMapper.toFullDto(event);
            info.setConfirmedRequests(confRequests.get(info.getId()));
            info.setViews(hits.get(event.getId()));
            result.add(info);
        }
        return result;
    }

    @Override
    @Transactional
    public OutputEventDto update(long eventId, UpdateEventAdminDto eventDto) {
        log.info("update event admin");
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException());
        Event updEvent = EventMapper.toEvent(eventDto, event);

        if (eventDto.getStateAction() != null) {
            if ((eventDto.getStateAction().toString().equals(State.PUBLISH_EVENT.toString())
                    || eventDto.getStateAction().toString().equals(State.REJECT_EVENT.toString()))
                    && (event.getState() == EventStatus.PUBLISHED || event.getState() == EventStatus.CANCELED)) {
                throw new ValidationException("Cannot update event with status " + event.getState());
            }
        }

        if (eventDto.getEventDate() != null
                && eventDto.getEventDate().isBefore(LocalDateTime.now().minusHours(2))) {
            throw new ValidationException("Something wrong with date");
        }

        EventStatus newState;
        if (eventDto.getStateAction().toString().equals(State.PUBLISH_EVENT.toString())) {
            newState = EventStatus.PUBLISHED;
        } else if (eventDto.getStateAction().toString().equals(State.CANCEL_REVIEW.toString())
                || eventDto.getStateAction().toString().equals(State.REJECT_EVENT.toString())
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
        List<Event> events = eventRepository.searchEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                sort, from, size);
        if (events.isEmpty()) {
            return List.of();
        }
        Map<Long, Long> hits = getTwo(events);
        List<Long> eventIds = new ArrayList<>();
        for (Event event: events) {
            eventIds.add(event.getId());
        }
        Map<Long, Integer> confRequests = requestRepository.getRequestsEventsConfirmed(eventIds);
        List<EventInfo> result = new ArrayList<>();
        for (Event event: events) {
            EventInfo info = EventMapper.toFullDto(event);
            info.setConfirmedRequests(confRequests.get(info.getId()));
            info.setViews(hits.get(event.getId()));
            result.add(info);
        }
        return result;
    }

    @Override
    public EventInfo getFullInfoById(long eventId) {
        log.info("get full information");
        EventInfo info = EventMapper.toFullDto(eventRepository.findById(eventId).orElseThrow(()
                -> new NoSuchElementException()));
        int requests = 0;
        List<HitDto> viewsStats = viewsStats((int) eventId);
        for (int i = 0, viewsStatsSize = viewsStats.size(); i < viewsStatsSize; i++) {
            requests++;
        }
        info.setConfirmedRequests(requests);
        Map<Long, Long> hits = get(List.of(eventRepository.getReferenceById(eventId)));
        log.info("hits {}", hits.toString());
        info.setViews(hits.get(eventId));
        return info;
    }

    private List<HitDto> viewsStats(int id) {
        String[] uris = new String[1];
        uris[0] = "/events/" + id;
        ResponseEntity<Object> hits = statsClient.getHits(min.format(formatter), max.format(formatter), uris,
                true);
        List<HitDto> hitList = new ObjectMapper().convertValue(hits.getBody(), new TypeReference<>() {
        });
        return hitList;
    }

    private Map<Long, Long> get(List<Event> events) {
        List<String> uris = new ArrayList<>();
        for (Event event: events) {
            uris.add(URI + event.getId().toString());
            log.info(uris.toString());
        }
        Event earliestEvent = Collections.min(events, Comparator.comparing(Event::getEventDate));
        log.info("uris {}", uris);
        List<HitStatDto> stats = client.get(earliestEvent.getEventDate().format(DATE_TIME_FORMATTER),
                LocalDateTime.now().format(DATE_TIME_FORMATTER), uris, true);
        log.info("stats {}", stats);
        Map<Long, Long> hits = new HashMap<>();
        for (HitStatDto viewStatsDto: stats) {
            Long id = Long.parseLong(viewStatsDto.getUri().split("/")[2]);
            log.info(id.toString());
            hits.put(id, viewStatsDto.getHits());
        }
        return hits;
    }

    private Map<Long, Long> getTwo(List<Event> events) {
        List<String> uris = new ArrayList<>();
        for (Event event: events) {
            uris.add(URI + event.getId().toString());
            log.info(uris.toString());
        }
        Event earliestEvent = Collections.min(events, Comparator.comparing(Event::getEventDate));
        log.info("uris {}", uris);
        List<HitStatDto> stats = client.get(earliestEvent.getEventDate().format(DATE_TIME_FORMATTER),
                LocalDateTime.now().format(DATE_TIME_FORMATTER), uris, true);
        log.info("stats {}", stats);
        Map<Long, Long> hits = new HashMap<>();
        for (HitStatDto viewStatsDto: stats) {
            log.info(viewStatsDto.getUri());
            Long id = Long.parseLong(viewStatsDto.getUri().split("/")[1]);
            log.info(id.toString());
            hits.put(id, viewStatsDto.getHits());
        }
        return hits;
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
