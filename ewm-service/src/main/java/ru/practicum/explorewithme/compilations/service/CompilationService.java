package ru.practicum.explorewithme.compilations.service;

import ru.practicum.explorewithme.compilations.dto.CompilationDto;
import ru.practicum.explorewithme.compilations.dto.CompilationDtoToCreate;
import ru.practicum.explorewithme.compilations.dto.CompilationDtoToUpdate;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getAll(Boolean pinned, int from, int size);

    CompilationDto getById(long compilationId);

    CompilationDto create(CompilationDtoToCreate compilationDtoToCreate);

    void delete(long compilationId);

    CompilationDto update(long compilationId, CompilationDtoToUpdate dto);
}
