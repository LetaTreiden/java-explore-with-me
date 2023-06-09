package ru.practicum.explorewithme.compilations.service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.compilations.dto.CompilationDtoToCreate;
import ru.practicum.explorewithme.compilations.dto.CompilationDto;
import ru.practicum.explorewithme.compilations.CompilationMapper;
import ru.practicum.explorewithme.compilations.dto.CompilationDtoToUpdate;
import ru.practicum.explorewithme.compilations.model.Compilation;
import ru.practicum.explorewithme.compilations.repository.CompilationRepository;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.exceptions.ValidationException;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

  private final CompilationRepository cRepo;
  private final EventRepository eRepo;

  @Override
  public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
    PageRequest pageRequest = PageRequest.of(from / size, size);
    List<Compilation> compilations = pinned != null
        ? cRepo.findAllByPinned(pinned, pageRequest)
        : cRepo.findAll(pageRequest).toList();

    return compilations.stream()
        .map(CompilationMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public CompilationDto getById(long compilationId) {
    return CompilationMapper
        .toDto(cRepo.findById(compilationId).orElseThrow(() -> new NoSuchElementException()));
  }

  @Transactional
  @Override
  public CompilationDto create(CompilationDtoToCreate compilationDtoToCreate) {
    Compilation compilation = CompilationMapper.toCompilation(compilationDtoToCreate);
    if (compilation != null) {
      compilation.setEvents(new HashSet<>(eRepo.findAllById(compilationDtoToCreate.getEvents())));
    } else {
      throw new ValidationException("Collection cannot be null");
    }
    cRepo.save(compilation);
    return CompilationMapper.toDto(compilation);
  }

  @Override
  @Transactional
  public void delete(long compilationId) {
    cRepo.deleteById(compilationId);
  }

  @Override
  @Transactional
  public CompilationDto update(long compilationId, CompilationDtoToUpdate dto) {
    Compilation prevComp = cRepo.findById(compilationId).orElseThrow(() -> new NoSuchElementException());
    Compilation compilation = CompilationMapper.toUpdatedComp(dto, prevComp);
    compilation.setEvents(new HashSet<>(eRepo.findAllById(dto.getEvents())));
    cRepo.save(compilation);
    return CompilationMapper.toDto(compilation);
  }
}
