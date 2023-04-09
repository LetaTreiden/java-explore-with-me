package ru.practicum.explorewithme.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateEventDto {
    private Long id;

    @Size(min = 1, max = 2000)
    private String annotation;

    private Integer category;

    @Size(min = 1, max = 7000)
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDate;

    private Location location;
    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;

    @Size(min = 1, max = 120)
    private String title;

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
