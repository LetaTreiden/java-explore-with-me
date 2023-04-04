package ru.practicum.explorewithme.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.request.dto.RequestDto;
import ru.practicum.explorewithme.request.model.Request;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {

  public static RequestDto toDto(Request request) {
    return RequestDto.builder()
        .id(request.getId())
        .requester(request.getRequester().getId())
        .event(request.getEvent().getId())
        .created(request.getCreated())
        .status(request.getStatus())
        .build();
  }
}
