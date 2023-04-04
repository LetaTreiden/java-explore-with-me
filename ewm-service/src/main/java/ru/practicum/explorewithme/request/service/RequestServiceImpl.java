package ru.practicum.explorewithme.request.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.commonhandler.ValidationException;
import ru.practicum.explorewithme.event.dto.EventStatus;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.request.dto.ChangeRequestStatusDto;
import ru.practicum.explorewithme.request.dto.RequestDto;
import ru.practicum.explorewithme.request.dto.RequestMapper;
import ru.practicum.explorewithme.request.dto.RequestStatusDto;
import ru.practicum.explorewithme.request.dto.RequestStatusesDto;
import ru.practicum.explorewithme.request.model.RequestEntity;
import ru.practicum.explorewithme.request.repository.RequestRepository;
import ru.practicum.explorewithme.user.model.User;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

  private final RequestRepository requestRepository;
  private final EventRepository eventRepository;

  @Override
  public List<RequestDto> getRequests(long userId) {
    return requestRepository.findAllByUserId(userId).stream()
        .map(RequestMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public RequestDto createRequest(long userId, long eventId) {
    if (requestRepository.existsByEventIdAndUserId(eventId, userId)) {
      throw new ValidationException("This request already exists.");
    }

    var event = eventRepository.findById(eventId).orElseThrow(NoSuchElementException::new);
    if (event.getState() != EventStatus.PUBLISHED) {
      throw new ValidationException("Event not published yet.");
    }

    if (event.getInitiator().getId() == userId) {
      throw new ValidationException("Initiator cant send requests for its events.");
    }

    if (event.getParticipantLimit() != null) {
      if (event.getConfirmedRequests() != null && event.getConfirmedRequests() >= event.getParticipantLimit()) {
        throw new ValidationException("Participant limit reached.");
      }
    }

    var requestState = Boolean.TRUE.equals(event.getRequestModeration())
        ? RequestStatusDto.PENDING
        : RequestStatusDto.CONFIRMED;

    var user = new User();
    user.setId(userId);

    var request = RequestEntity.builder()
        .event(event)
        .user(user)
        .createdOn(LocalDateTime.now())
        .status(requestState)
        .build();

    requestRepository.save(request);

    if (requestState == RequestStatusDto.CONFIRMED) {
      event.setConfirmedRequests(event.getConfirmedRequests() != null ? event.getConfirmedRequests() + 1 : 1);
      eventRepository.save(event);
    }

    return RequestMapper.toDto(request);
  }

  @Override
  public RequestDto cancelRequest(long userId, long requestId) {
    var request = requestRepository.findById(requestId).orElseThrow(NoSuchElementException::new);

    if (request.getUser().getId() != userId) {
      throw new IllegalStateException("Only request owner can cancel requests.");
    }

    request.setStatus(RequestStatusDto.CANCELED);
    requestRepository.save(request);

    return RequestMapper.toDto(request);
  }

  @Override
  public List<RequestDto> getEventRequests(long userId, long eventId) {
    var event = eventRepository.findById(eventId).orElseThrow(NoSuchElementException::new);

    if (event.getInitiator().getId() != userId) {
      throw new IllegalStateException("Only request owner can watch requests for its event.");
    }

    return requestRepository.findAllByEventId(eventId).stream()
        .map(RequestMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public RequestStatusesDto changeEventRequests(long userId, long eventId,
      ChangeRequestStatusDto changeRequestStatusDto) {
    var requests = requestRepository.findAllByIdIn(changeRequestStatusDto.getRequestIds());
    requests.forEach(s -> {
      if (s.getStatus() != RequestStatusDto.PENDING) {
        throw new ValidationException("Cannot change request status.");
      }
      s.setStatus(changeRequestStatusDto.getStatus());

      var event = eventRepository.findById(eventId).orElseThrow(NoSuchElementException::new);

      if (event.getParticipantLimit() != null) {
        if (event.getConfirmedRequests() != null && event.getConfirmedRequests() >= event.getParticipantLimit()) {
          throw new ValidationException("Participant limit reached.");
        }
      }

      event.setConfirmedRequests(event.getConfirmedRequests() != null ? event.getConfirmedRequests() + 1 : 1);
      requestRepository.save(s);
      eventRepository.save(event);
    });

    var updatedRequests = new RequestStatusesDto();
    if (changeRequestStatusDto.getStatus() == RequestStatusDto.CONFIRMED) {
      updatedRequests.setConfirmedRequests(requests.stream().map(RequestMapper::toDto).collect(Collectors.toList()));
    } else {
      updatedRequests.setRejectedRequests(requests.stream().map(RequestMapper::toDto).collect(Collectors.toList()));
    }

    return updatedRequests;
  }
}
