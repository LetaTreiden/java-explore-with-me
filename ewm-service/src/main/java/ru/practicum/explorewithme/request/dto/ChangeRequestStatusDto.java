package ru.practicum.explorewithme.request.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeRequestStatusDto {

  private List<Long> requestIds;
  private RequestStatusDto status;
}
