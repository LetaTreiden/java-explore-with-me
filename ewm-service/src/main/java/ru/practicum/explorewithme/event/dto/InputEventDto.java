package ru.practicum.explorewithme.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import lombok.*;
import ru.practicum.explorewithme.event.model.State;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InputEventDto {

  @NotBlank
  @Size(min = 1, max = 2000)
  private String annotation;

  @NotNull
  private Integer category;

  @NotBlank
  @Size(min = 1, max = 7000)
  private String description;

  @NotNull
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
  private LocalDateTime eventDate;

  @NotNull
  private Location location;
  private boolean paid;

  @PositiveOrZero
  private int participantLimit;

  private boolean requestModeration;

  @NotBlank
  @Size(min = 1, max = 120)
  private String title;

  private State state;

  public Boolean getPaid() {
    return paid;
  }

  public Boolean getRequestModeration() {
    return requestModeration;
  }

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
