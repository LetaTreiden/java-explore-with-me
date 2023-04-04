package ru.practicum.explorewithme.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.category.model.CategoryEntity;
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
  private CategoryEntity category;
  private String description;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
  private LocalDateTime eventDate;
  private LocationDto location;
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
}
