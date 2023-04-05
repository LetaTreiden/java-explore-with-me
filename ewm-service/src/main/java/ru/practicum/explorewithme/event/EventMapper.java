package ru.practicum.explorewithme.event;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.event.dto.*;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.Location;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

  public static OutputEventDto toDto(Event event) {
    return OutputEventDto.builder()
        .id(event.getId())
        .annotation(event.getAnnotation())
        .description(event.getDescription())
        .eventDate(event.getEventDate())
        .paid(event.getPaid())
        .participantLimit(event.getParticipantLimit())
        .requestModeration(event.getRequestModeration())
        .title(event.getTitle())
        .createdOn(event.getCreatedOn())
        .location(new OutputEventDto.Location(event.getLocation().getLat(), event.getLocation().getLon()))
        .publishedOn(event.getPublishedOn())
        .state(event.getState())
        .initiator(event.getInitiator())
        .category(event.getCategory())
        .confirmedRequests(event.getConfirmedRequests())
        .views(event.getViews())
        .build();
  }

  public static Event toEvent(InputEventDto inputEventDto) {
    return Event.builder()
        .annotation(inputEventDto.getAnnotation())
        .description(inputEventDto.getDescription())
        .eventDate(inputEventDto.getEventDate())
        .paid(inputEventDto.getPaid())
        .participantLimit(inputEventDto.getParticipantLimit())
        .requestModeration(inputEventDto.getRequestModeration())
        .title(inputEventDto.getTitle())
        .createdOn(LocalDateTime.now())
        .location(new Location(inputEventDto.getLocation().getLat(), inputEventDto.getLocation().getLon()))
        .build();
  }

  public static Event toEvent(InputEventDto inputEventDto, Event event) {
    EventStatus newState = null;
    if (inputEventDto.getState() != null) {
      newState = inputEventDto.getState() == State.CANCEL_REVIEW
          ? EventStatus.CANCELED
          : EventStatus.PENDING;
    }

    return Event.builder()
        .id(event.getId())
        .annotation(inputEventDto.getAnnotation() != null ? inputEventDto.getAnnotation() : event.getAnnotation())
        .description(inputEventDto.getDescription() != null
            ? inputEventDto.getDescription()
            : event.getDescription())
        .eventDate(inputEventDto.getEventDate() != null ? inputEventDto.getEventDate() : event.getEventDate())
        .paid(inputEventDto.getPaid() != null ? inputEventDto.getPaid() : event.getPaid())
        .participantLimit(inputEventDto.getParticipantLimit() != null
            ? inputEventDto.getParticipantLimit()
            : event.getParticipantLimit())
        .requestModeration(inputEventDto.getRequestModeration() != null
            ? inputEventDto.getRequestModeration()
            : event.getRequestModeration())
        .title(inputEventDto.getTitle() != null ? inputEventDto.getTitle() : event.getTitle())
        .location(inputEventDto.getLocation() != null ?
                new Location(inputEventDto.getLocation().getLat(), inputEventDto.getLocation().getLon())
                : event.getLocation())
        .createdOn(event.getCreatedOn())
        .state(newState != null ? newState : event.getState())
        .initiator(event.getInitiator())
        .category(event.getCategory())
        .confirmedRequests(event.getConfirmedRequests())
        .views(event.getViews())
        .build();
  }

  public static EventInfo toFullDto(Event event) {
    return EventInfo.builder()
        .id(event.getId())
        .annotation(event.getAnnotation())
        .description(event.getDescription())
        .eventDate(event.getEventDate())
        .paid(event.getPaid())
        .participantLimit(event.getParticipantLimit())
        .requestModeration(event.getRequestModeration())
        .title(event.getTitle())
        .createdOn(event.getCreatedOn())
        .location(new EventInfo.Location(event.getLocation().getLat(), event.getLocation().getLon()))
        .publishedOn(event.getPublishedOn())
        .state(event.getState())
        .initiator(new EventInfo.User(event.getInitiator().getId(), event.getInitiator().getName()))
        .category(new EventInfo.Category(event.getCategory().getId(), event.getCategory().getName()))
        .confirmedRequests(event.getConfirmedRequests())
        .views(event.getViews())
        .build();
  }
}
