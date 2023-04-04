package ru.practicum.explorewithme.compilations.service;

import java.util.List;
import ru.practicum.explorewithme.compilations.dto.CompilationDtoToCreate;
import ru.practicum.explorewithme.compilations.dto.CompilationDto;

public interface CompilationService {

  List<CompilationDto> getAll(Boolean pinned, int from, int size);

  CompilationDto getById(long compilationId);

  CompilationDto create(CompilationDtoToCreate compilationDtoToCreate);

  void delete(long compilationId);

  CompilationDto update(long compilationId, CompilationDtoToCreate compilationDtoToCreate);
}
