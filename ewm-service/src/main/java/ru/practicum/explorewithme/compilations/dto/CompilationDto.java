package ru.practicum.explorewithme.compilations.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.event.dto.FullEventInfo;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {

  private List<FullEventInfo> events;
  private Long id;
  private Boolean pinned;
  private String title;
}
