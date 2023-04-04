package ru.practicum.explorewithme.compilations.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationCreationDto {

  private List<Long> events;
  private Boolean pinned;
  @NotBlank
  private String title;
}
