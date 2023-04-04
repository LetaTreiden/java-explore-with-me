package ru.practicum.explorewithme.compilations.service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.compilations.dto.CompilationCreationDto;
import ru.practicum.explorewithme.compilations.dto.CompilationDto;
import ru.practicum.explorewithme.compilations.dto.CompilationMapper;
import ru.practicum.explorewithme.compilations.repository.CompilationRepository;
import ru.practicum.explorewithme.event.repository.EventRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

  private final CompilationRepository compilationRepository;
  private final EventRepository eventRepository;

  @Override
  public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
    var pageRequest = PageRequest.of(from / size, size);
    var compilations = pinned != null
        ? compilationRepository.findAllByPinned(pinned, pageRequest)
        : compilationRepository.findAll(pageRequest).toList();

    return compilations.stream()
        .map(CompilationMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public CompilationDto getCompilation(long compilationId) {
    return CompilationMapper
        .toDto(compilationRepository.findById(compilationId).orElseThrow(NoSuchElementException::new));
  }

  @Transactional
  @Override
  public CompilationDto createCompilation(CompilationCreationDto compilationCreationDto) {
    var compilation = CompilationMapper.toEntity(compilationCreationDto);
    compilation.setEvents(new HashSet<>(eventRepository.findAllById(compilationCreationDto.getEvents())));
    compilationRepository.save(compilation);
    return CompilationMapper.toDto(compilation);
  }

  @Override
  public void deleteCompilation(long compilationId) {
    compilationRepository.deleteById(compilationId);
  }

  @Override
  public CompilationDto updateCompilation(long compilationId, CompilationCreationDto compilationCreationDto) {
    var previousCompilation = compilationRepository.findById(compilationId).orElseThrow(NoSuchElementException::new);
    var compilation = CompilationMapper.toUpdatedEntity(compilationCreationDto, previousCompilation);
    compilation.setEvents(new HashSet<>(eventRepository.findAllById(compilationCreationDto.getEvents())));
    compilationRepository.save(compilation);
    return CompilationMapper.toDto(compilation);
  }
}
