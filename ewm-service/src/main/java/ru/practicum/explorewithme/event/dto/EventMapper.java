package ru.practicum.explorewithme.event.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.event.model.EventEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

  public static OutputEventDto toDto(EventEntity eventEntity) {
    return OutputEventDto.builder()
        .id(eventEntity.getId())
        .annotation(eventEntity.getAnnotation())
        .description(eventEntity.getDescription())
        .eventDate(eventEntity.getEventDate())
        .paid(eventEntity.getPaid())
        .participantLimit(eventEntity.getParticipantLimit())
        .requestModeration(eventEntity.getRequestModeration())
        .title(eventEntity.getTitle())
        .createdOn(eventEntity.getCreatedOn())
        .location(new LocationDto(eventEntity.getLocation().getLat(), eventEntity.getLocation().getLon()))
        .publishedOn(eventEntity.getPublishedOn())
        .state(eventEntity.getState())
        .initiator(eventEntity.getInitiator())
        .category(eventEntity.getCategory())
        .confirmedRequests(eventEntity.getConfirmedRequests())
        .views(eventEntity.getViews())
        .build();
  }

  public static EventEntity toEntity(InputEventDto inputEventDto) {
    return EventEntity.builder()
        .annotation(inputEventDto.getAnnotation())
        .description(inputEventDto.getDescription())
        .eventDate(inputEventDto.getEventDate())
        .paid(inputEventDto.getPaid())
        .participantLimit(inputEventDto.getParticipantLimit())
        .requestModeration(inputEventDto.getRequestModeration())
        .title(inputEventDto.getTitle())
        .createdOn(LocalDateTime.now())
        .location(inputEventDto.getLocation())
        .build();
  }

  public static EventEntity toEntity(InputEventDto inputEventDto, EventEntity eventEntity) {
    EventStatus newState = null;
    if (inputEventDto.getStateAction() != null) {
      newState = inputEventDto.getStateAction() == StateAction.CANCEL_REVIEW
          ? EventStatus.CANCELED
          : EventStatus.PENDING;
    }

    return EventEntity.builder()
        .id(eventEntity.getId())
        .annotation(inputEventDto.getAnnotation() != null ? inputEventDto.getAnnotation() : eventEntity.getAnnotation())
        .description(inputEventDto.getDescription() != null
            ? inputEventDto.getDescription()
            : eventEntity.getDescription())
        .eventDate(inputEventDto.getEventDate() != null ? inputEventDto.getEventDate() : eventEntity.getEventDate())
        .paid(inputEventDto.getPaid() != null ? inputEventDto.getPaid() : eventEntity.getPaid())
        .participantLimit(inputEventDto.getParticipantLimit() != null
            ? inputEventDto.getParticipantLimit()
            : eventEntity.getParticipantLimit())
        .requestModeration(inputEventDto.getRequestModeration() != null
            ? inputEventDto.getRequestModeration()
            : eventEntity.getRequestModeration())
        .title(inputEventDto.getTitle() != null ? inputEventDto.getTitle() : eventEntity.getTitle())
        .location(inputEventDto.getLocation() != null ? inputEventDto.getLocation() : eventEntity.getLocation())
        .createdOn(eventEntity.getCreatedOn())
        .state(newState != null ? newState : eventEntity.getState())
        .initiator(eventEntity.getInitiator())
        .category(eventEntity.getCategory())
        .confirmedRequests(eventEntity.getConfirmedRequests())
        .views(eventEntity.getViews())
        .build();
  }

  public static FullEventInfo toFullDto(EventEntity eventEntity) {
    return FullEventInfo.builder()
        .id(eventEntity.getId())
        .annotation(eventEntity.getAnnotation())
        .description(eventEntity.getDescription())
        .eventDate(eventEntity.getEventDate())
        .paid(eventEntity.getPaid())
        .participantLimit(eventEntity.getParticipantLimit())
        .requestModeration(eventEntity.getRequestModeration())
        .title(eventEntity.getTitle())
        .createdOn(eventEntity.getCreatedOn())
        .location(new LocationDto(eventEntity.getLocation().getLat(), eventEntity.getLocation().getLon()))
        .publishedOn(eventEntity.getPublishedOn())
        .state(eventEntity.getState())
        .initiator(eventEntity.getInitiator())
        .category(eventEntity.getCategory())
        .confirmedRequests(eventEntity.getConfirmedRequests())
        .views(eventEntity.getViews())
        .build();
  }
}
