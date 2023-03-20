package ru.practicum.explorewithme;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HitDto {

  private String app;
  private String uri;
  private String ip;
  private String timestamp;
}
