package ru.practicum.explorewithme.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.event.model.State;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateEventDto {
  private State state;
}
