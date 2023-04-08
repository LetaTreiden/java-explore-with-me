package ru.practicum.explorewithme.event;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.explorewithme.event.dto.*;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventStatus;
import ru.practicum.explorewithme.event.model.Location;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
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
        .initiator(new OutputEventDto.User(event.getInitiator().getId(), event.getInitiator().getName()))
        .category(new OutputEventDto.Category(event.getCategory().getId(), event.getCategory().getName()))
        .views(0L)
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

  public static Event toEvent(UpdateEventDto eventDto, Event event) {
    log.info("right mapper");
    EventStatus newState = null;
    if (eventDto.getState() != null) {
      switch (eventDto.getState()) {
        case CANCEL_REVIEW:
        case DECLINE_EVENT:
        case REJECT_EVENT:
          newState = EventStatus.CANCELED;
          break;
        case PUBLISH_EVENT:
          newState = EventStatus.PUBLISHED;
          break;
        case SEND_TO_REVIEW:
          newState = EventStatus.PENDING;
          break;
      }
    }
    return Event.builder()
            .id(event.getId())
            .annotation(eventDto.getAnnotation() != null && !eventDto.getAnnotation().isBlank()
                    ? eventDto.getAnnotation() : event.getAnnotation())
            .description(eventDto.getDescription() != null && !eventDto.getDescription().isBlank()
                    ? eventDto.getDescription()
                    : event.getDescription())
            .eventDate(eventDto.getEventDate() != null ? eventDto.getEventDate() : event.getEventDate())
            .paid(eventDto.getPaid() != null ? eventDto.getPaid() : event.getPaid())
            .participantLimit(eventDto.getParticipantLimit() != null
                    ? eventDto.getParticipantLimit()
                    : event.getParticipantLimit())
            .requestModeration(eventDto.getRequestModeration() != null
                    ? eventDto.getRequestModeration()
                    : event.getRequestModeration())
            .title(eventDto.getTitle() != null && !eventDto.getTitle().isBlank()
                    ? eventDto.getTitle() : event.getTitle())
            .location(eventDto.getLocation() != null ?
                    new Location(eventDto.getLocation().getLat(), eventDto.getLocation().getLon())
                    : event.getLocation())
            .createdOn(event.getCreatedOn())
            .state(newState != null ? newState : event.getState())
            .initiator(event.getInitiator())
            .category(event.getCategory())
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
        .views(0L)
        .build();
  }
}
