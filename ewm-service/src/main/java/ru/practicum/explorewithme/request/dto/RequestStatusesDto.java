package ru.practicum.explorewithme.request.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestStatusesDto {

  private List<RequestDto> confirmedRequests;
  private List<RequestDto> rejectedRequests;
}
