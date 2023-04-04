package ru.practicum.explorewithme.request.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.request.model.RequestEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {

  public static RequestDto toDto(RequestEntity requestEntity) {
    return RequestDto.builder()
        .id(requestEntity.getId())
        .requesterId(requestEntity.getUser().getId())
        .eventId(requestEntity.getEvent().getId())
        .created(requestEntity.getCreatedOn())
        .status(requestEntity.getStatus())
        .build();
  }
}
