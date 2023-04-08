package ru.practicum.explorewithme.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventStatus;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.exceptions.ValidationException;
import ru.practicum.explorewithme.request.RequestMapper;
import ru.practicum.explorewithme.request.dto.RequestDto;
import ru.practicum.explorewithme.request.dto.RequestStatusesDto;
import ru.practicum.explorewithme.request.dto.UpdateRequestDto;
import ru.practicum.explorewithme.request.model.Request;
import ru.practicum.explorewithme.request.model.RequestStatus;
import ru.practicum.explorewithme.request.repository.RequestRepository;
import ru.practicum.explorewithme.user.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;
import static ru.practicum.explorewithme.request.RequestMapper.toDto;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

    @Override
    public List<RequestDto> getAll(long userId) {
        return requestRepository.findAllByUserId(userId).stream()
                .map(request -> toDto(request))
                .collect(toList());
    }

    @Override
    public RequestDto create(long userId, long eventId) {
        log.info("creating request");
        if (requestRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new ValidationException("Request already exists");
        }
        log.info("there is no such request yet");
        Event event;
        if (eventRepository.existsById(eventId)) {
            event = eventRepository.getReferenceById(eventId);
        } else {
            log.info("This user id{} doesn't exists", userId);
            throw new NoSuchElementException("There is no such user");
        }
        log.info("user {} exist", userId);
        if (event.getState() != EventStatus.PUBLISHED) {
            throw new ValidationException("You cannot add a request to event with status " + event.getState());
        }
        log.info("you can do a request");

        if (event.getInitiator().getId() == userId) {
            throw new ValidationException("You are already invited:)");
        }
        log.info("you wasn't invited yet");

        if (event.getParticipantLimit() != null) {
            if (requestRepository.getRequestsEventConfirmed((int) eventId).size() >= event.getParticipantLimit()) {
                log.info("Too much participants");
                throw new ValidationException("Too much participants");
            }
        }
        log.info("there is enough places for participants");

        RequestStatus requestState;
        if (Boolean.TRUE.equals(event.getRequestModeration())) requestState = RequestStatus.PENDING;
        else requestState = RequestStatus.CONFIRMED;
        log.info("Request status is {}", requestState);

        User user = new User();
        user.setId(userId);

        Request request = Request.builder()
                .event(event)
                .user(user)
                .createdOn(LocalDateTime.now())
                .status(requestState)
                .build();
        log.info("request created");

        requestRepository.save(request);
        log.info("id is {}", request.getId());

        return toDto(request);
    }

    @Override
    public RequestDto cancel(long userId, long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NoSuchElementException());

        if (request.getUser().getId() != userId) {
            throw new IllegalStateException("You don't have such rights");
        }

        request.setStatus(RequestStatus.CANCELED);
        requestRepository.save(request);
        log.info(String.valueOf(request.getId()));
        return toDto(request);
    }

    @Override
    public List<RequestDto> getAllById(long userId, long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException());

        if (event.getInitiator().getId() != userId) {
            throw new IllegalStateException("You don't have such rights");
        }

        return requestRepository.findAllByEventId(eventId).stream()
                .map(request -> toDto(request))
                .collect(toList());
    }

    @Override
    public RequestStatusesDto update(long userId, long eventId,
                                     UpdateRequestDto updateRequestDto) {
        List<Request> requests = requestRepository.findAllByIdIn(updateRequestDto.getRequestIds());
        requests.forEach(s -> {
            if (s.getStatus() != RequestStatus.PENDING) {
                throw new ValidationException("You cannot change request with status " + s.getStatus());
            }
            s.setStatus(updateRequestDto.getStatus());

            Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException());

            if (requestRepository.getRequestsEventConfirmed((int) eventId).size() >= event.getParticipantLimit()) {
                log.info("Too much participants");
                throw new ValidationException("Too much participants");
            }

            requestRepository.save(s);
            eventRepository.save(event);
        });

        RequestStatusesDto updatedRequests = new RequestStatusesDto();
        if (updateRequestDto.getStatus() == RequestStatus.CONFIRMED) {
            updatedRequests.setConfirmedRequests(requests.stream().map(RequestMapper::toDto)
                    .collect(toList()));
            updatedRequests.setRejectedRequests(List.of());
        } else {
            updatedRequests.setRejectedRequests(requests.stream().map(RequestMapper::toDto)
                    .collect(toList()));
            updatedRequests.setConfirmedRequests(List.of());
        }
        return updatedRequests;
    }
}
