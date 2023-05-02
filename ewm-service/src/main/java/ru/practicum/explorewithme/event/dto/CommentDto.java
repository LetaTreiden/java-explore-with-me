package ru.practicum.explorewithme.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    Long id;
    LocalDateTime created;
    Long eventId;
    User author;
    String text;

    @Data
    @AllArgsConstructor
    public static class User {
        private long id;
        private String name;
    }
}