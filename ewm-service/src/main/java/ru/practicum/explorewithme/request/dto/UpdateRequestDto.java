package ru.practicum.explorewithme.request.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.explorewithme.request.model.RequestStatus;

@Getter
@Setter
public class UpdateRequestDto {

  private List<Long> requestIds;
  private RequestStatus status;
}
