package ru.practicum.explorewithme.compilations.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDtoToUpdate {
    private List<Long> events;
    private boolean pinned;

    @NotBlank
    @Size(max = 120, min = 1)
    private String title;

    public Boolean getPinned() {
        return pinned;
    }
}
