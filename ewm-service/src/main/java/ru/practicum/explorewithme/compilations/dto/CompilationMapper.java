package ru.practicum.explorewithme.compilations.dto;

import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.compilations.model.CompilationEntity;
import ru.practicum.explorewithme.event.dto.EventMapper;
import ru.practicum.explorewithme.event.model.EventEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompilationMapper {

  public static CompilationDto toDto(CompilationEntity compilationEntity) {
    return CompilationDto.builder()
        .id(compilationEntity.getId())
        .pinned(compilationEntity.getPinned())
        .title(compilationEntity.getTitle())
        .events(compilationEntity.getEvents().stream()
            .map(EventMapper::toFullDto)
            .collect(Collectors.toList()))
        .build();
  }

  public static CompilationEntity toEntity(CompilationCreationDto compilationDto) {
    return CompilationEntity.builder()
        .title(compilationDto.getTitle())
        .pinned(compilationDto.getPinned())
        .events(compilationDto.getEvents().stream().map(EventEntity::new).collect(Collectors.toSet()))
        .build();
  }

  public static CompilationEntity toUpdatedEntity(CompilationCreationDto compilationDto,
      CompilationEntity previousCompilation) {
    return CompilationEntity.builder()
        .id(previousCompilation.getId())
        .title(compilationDto.getTitle() != null ? compilationDto.getTitle() : previousCompilation.getTitle())
        .pinned(compilationDto.getPinned() != null ? compilationDto.getPinned() : previousCompilation.getPinned())
        .events(compilationDto.getEvents() != null
            ? compilationDto.getEvents().stream().map(EventEntity::new).collect(Collectors.toSet())
            : previousCompilation.getEvents())
        .build();
  }
}
