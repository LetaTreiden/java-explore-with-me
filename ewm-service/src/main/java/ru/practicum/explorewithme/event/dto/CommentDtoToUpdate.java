package ru.practicum.explorewithme.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDtoToUpdate {
    @NotBlank
    @Size(min = 1, max = 3000)
    String text;
}
