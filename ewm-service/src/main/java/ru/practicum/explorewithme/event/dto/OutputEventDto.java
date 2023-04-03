package ru.practicum.explorewithme.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

import lombok.*;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.user.model.User;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OutputEventDto {

  private Long id;
  private String annotation;
  private User initiator;
  private Category category;
  private String description;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
  private LocalDateTime eventDate;

  private Location location;
  private Boolean paid;
  private Integer participantLimit;
  private Boolean requestModeration;
  private String title;
  private EventStatus state;
  private Long views;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
  private LocalDateTime createdOn;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
  private LocalDateTime publishedOn;

  private Integer confirmedRequests;

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