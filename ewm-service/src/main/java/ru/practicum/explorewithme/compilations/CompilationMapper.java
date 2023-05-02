package ru.practicum.explorewithme.compilations;

import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.compilations.dto.CompilationDto;
import ru.practicum.explorewithme.compilations.dto.CompilationDtoToCreate;
import ru.practicum.explorewithme.compilations.dto.CompilationDtoToUpdate;
import ru.practicum.explorewithme.compilations.model.Compilation;
import ru.practicum.explorewithme.event.EventMapper;
import ru.practicum.explorewithme.event.model.Event;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompilationMapper {

  public static CompilationDto toDto(Compilation compilationEntity) {
    return CompilationDto.builder()
        .id(compilationEntity.getId())
        .pinned(compilationEntity.getPinned())
        .title(compilationEntity.getTitle())
        .events(compilationEntity.getEvents().stream()
            .map(EventMapper::toFullDto)
            .collect(Collectors.toList()))
        .build();
  }

  public static Compilation toCompilation(CompilationDtoToCreate compilationDto) {
    Compilation compilation = new Compilation();
    compilation.setTitle(compilationDto.getTitle());
    compilation.setPinned(compilationDto.isPinned());
    if (!compilationDto.getEvents().isEmpty()) {
      compilation.setEvents(compilationDto.getEvents().stream().map(Event::new).collect(Collectors.toSet()));
    }
    return compilation;
  }

  public static Compilation toUpdatedComp(CompilationDtoToUpdate compilationDto,
                                          Compilation previousCompilation) {
    return Compilation.builder()
        .id(previousCompilation.getId())
        .title(compilationDto.getTitle()  != null && !compilationDto.getTitle().isBlank()
                ? compilationDto.getTitle() : previousCompilation.getTitle())
        .pinned(compilationDto.getPinned() != null ? compilationDto.getPinned() : previousCompilation.getPinned())
        .events(compilationDto.getEvents() != null
            ? compilationDto.getEvents().stream().map(Event::new).collect(Collectors.toSet())
            : previousCompilation.getEvents())
        .build();
  }
}
