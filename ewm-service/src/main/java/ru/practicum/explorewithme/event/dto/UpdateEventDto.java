package ru.practicum.explorewithme.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.explorewithme.event.model.State;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateEventDto {
    @NotBlank
    private String annotation;

    @NotNull
    private Integer category;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDate;

    private ru.practicum.explorewithme.event.dto.InputEventDto.Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;

    @NotBlank
    private String title;
    private State state;
    private State stateAction;

    @Data
    @Getter
    @AllArgsConstructor
    public class Location {
      //долгота
      private float lat;
      //широта
      private float lon;
    }
  }
