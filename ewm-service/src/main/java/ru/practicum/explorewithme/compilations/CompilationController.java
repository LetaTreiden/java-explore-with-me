package ru.practicum.explorewithme.compilations;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.compilations.dto.CompilationDtoToCreate;
import ru.practicum.explorewithme.compilations.dto.CompilationDto;
import ru.practicum.explorewithme.compilations.service.CompilationService;

@RestController
@RequiredArgsConstructor
public class CompilationController {

  private final CompilationService compilationService;

  @GetMapping("/compilations")
  public List<CompilationDto> getAll(@RequestParam(defaultValue = "false") Boolean pinned,
      @RequestParam(defaultValue = "0") int from,
      @RequestParam(defaultValue = "10") int size) {
    return compilationService.getAll(pinned, from, size);
  }

  @GetMapping("/compilations/{compilationId}")
  public CompilationDto getById(@PathVariable long compilationId) {
    return compilationService.getById(compilationId);
  }

  @PostMapping("/admin/compilations")
  @ResponseStatus(HttpStatus.CREATED)
  public CompilationDto create(@Valid @RequestBody CompilationDtoToCreate compilationDtoToCreate) {
    return compilationService.create(compilationDtoToCreate);
  }

  @DeleteMapping("/admin/compilations/{compilationId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable long compilationId) {
    compilationService.delete(compilationId);
  }

  @PatchMapping("/admin/compilations/{compilationId}")
  public CompilationDto update(@PathVariable long compilationId,
      @RequestBody CompilationDtoToCreate compilationDtoToCreate) {
    return compilationService.update(compilationId, compilationDtoToCreate);
  }
}
