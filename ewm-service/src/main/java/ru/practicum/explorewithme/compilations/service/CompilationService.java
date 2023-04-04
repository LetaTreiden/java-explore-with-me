package ru.practicum.explorewithme.compilations.service;

import java.util.List;
import ru.practicum.explorewithme.compilations.dto.CompilationCreationDto;
import ru.practicum.explorewithme.compilations.dto.CompilationDto;

public interface CompilationService {

  List<CompilationDto> getCompilations(Boolean pinned, int from, int size);

  CompilationDto getCompilation(long compilationId);

  CompilationDto createCompilation(CompilationCreationDto compilationCreationDto);

  void deleteCompilation(long compilationId);

  CompilationDto updateCompilation(long compilationId, CompilationCreationDto compilationCreationDto);
}
