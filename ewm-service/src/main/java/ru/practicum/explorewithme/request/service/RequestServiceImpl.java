package ru.practicum.explorewithme.request.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.dto.EventStatus;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.exceptions.ValidationException;
import ru.practicum.explorewithme.request.dto.UpdateRequestDto;
import ru.practicum.explorewithme.request.dto.RequestDto;
import ru.practicum.explorewithme.request.RequestMapper;
import ru.practicum.explorewithme.request.model.RequestStatus;
import ru.practicum.explorewithme.request.dto.RequestStatusesDto;
import ru.practicum.explorewithme.request.model.Request;
import ru.practicum.explorewithme.request.repository.RequestRepository;
import ru.practicum.explorewithme.user.model.User;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

  private final RequestRepository requestRepository;
  private final EventRepository eventRepository;

  @Override
  public List<RequestDto> getAll(long userId) {
    return requestRepository.findAllByUserId(userId).stream()
        .map(RequestMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public RequestDto create(long userId, long eventId) {
    if (requestRepository.existsByEventIdAndUserId(eventId, userId)) {
      throw new ValidationException("Request already exists");
    }

    Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException());
    if (event.getState() != EventStatus.PUBLISHED) {
      throw new ValidationException("You cannot add a request to event with status " + event.getState());
    }

    if (event.getInitiator().getId() == userId) {
      throw new ValidationException("You are already invited:)");
    }

    if (event.getParticipantLimit() != null) {
      if (event.getConfirmedRequests() != null && event.getConfirmedRequests() >= event.getParticipantLimit()) {
        throw new ValidationException("Too much participants");
      }
    }

    RequestStatus requestState = Boolean.TRUE.equals(event.getRequestModeration())
        ? RequestStatus.PENDING
        : RequestStatus.CONFIRMED;

    User user = new User();
    user.setId(userId);

    Request request = Request.builder()
        .event(event)
        .requester(user)
        .createdOn(LocalDateTime.now())
        .status(requestState)
        .build();

    requestRepository.save(request);

    if (requestState == RequestStatus.CONFIRMED) {
      event.setConfirmedRequests(event.getConfirmedRequests() != null ? event.getConfirmedRequests() + 1 : 1);
      eventRepository.save(event);
    }

    return RequestMapper.toDto(request);
  }

  @Override
  public RequestDto cancel(long userId, long requestId) {
    Request request = requestRepository.findById(requestId).orElseThrow(() -> new NoSuchElementException());

    if (request.getRequester().getId() != userId) {
      throw new IllegalStateException("You don't have such rights");
    }

    request.setStatus(RequestStatus.CANCELED);
    requestRepository.save(request);

    return RequestMapper.toDto(request);
  }

  @Override
  public List<RequestDto> getAllById(long userId, long eventId) {
    Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException());

    if (event.getInitiator().getId() != userId) {
      throw new IllegalStateException("You don't have such rights");
    }

    return requestRepository.findAllByEventId(eventId).stream()
        .map(RequestMapper::toDto)
        .collect(Collectors.toList());
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

      if (event.getParticipantLimit() != null) {
        if (event.getConfirmedRequests() != null && event.getConfirmedRequests() >= event.getParticipantLimit()) {
          throw new ValidationException("Too much participants");
        }
      }

      event.setConfirmedRequests(event.getConfirmedRequests() != null ? event.getConfirmedRequests() + 1 : 1);
      requestRepository.save(s);
      eventRepository.save(event);
    });

    RequestStatusesDto updatedRequests = new RequestStatusesDto();
    if (updateRequestDto.getStatus() == RequestStatus.CONFIRMED) {
      updatedRequests.setConfirmedRequests(requests.stream().map(RequestMapper::toDto).collect(Collectors.toList()));
    } else {
      updatedRequests.setRejectedRequests(requests.stream().map(RequestMapper::toDto).collect(Collectors.toList()));
    }

    return updatedRequests;
  }
}
