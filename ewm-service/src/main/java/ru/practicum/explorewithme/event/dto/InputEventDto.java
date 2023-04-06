package ru.practicum.explorewithme.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.*;
import ru.practicum.explorewithme.event.model.State;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InputEventDto {

  @NotBlank
  private String annotation;

  @NotNull
  private Integer category;

  private String description;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
  private LocalDateTime eventDate;

  private Location location;
  private Boolean paid;
  private Integer participantLimit;
  private Boolean requestModeration;

  @NotBlank
  private String title;
  private State state;

  @Data
  @Getter
  @AllArgsConstructor
  public static class Location {
    //долгота
    private float lat;
    //широта
    private float lon;
  }
}
