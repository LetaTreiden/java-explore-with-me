package ru.practicum.explorewithme.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.explorewithme.event.model.State;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateEventDto {
    private Long id;
    private String annotation;
    private Integer category;
    private String description;
    //private User initiator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDate;

    private Location location;
    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;
    
    private String title;
    private State state;
    private State stateAction;

    @Data
    @Getter
    @AllArgsConstructor
    public static class Location {
      //долгота
      private float lat;
      //широта
      private float lon;
    }

    @Data
    @AllArgsConstructor
    public static class User {
        private long id;
        private String name;
    }
  }
