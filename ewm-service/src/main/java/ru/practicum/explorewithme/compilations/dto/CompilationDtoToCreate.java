package ru.practicum.explorewithme.compilations.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
public class CompilationDtoToCreate {

  private List<Long> events;
  private boolean pinned;

  @NotBlank
  @Size(max = 120, min = 1)
  private String title;

}
