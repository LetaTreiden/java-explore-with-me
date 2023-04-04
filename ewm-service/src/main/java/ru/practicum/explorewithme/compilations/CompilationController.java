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
import ru.practicum.explorewithme.compilations.dto.CompilationCreationDto;
import ru.practicum.explorewithme.compilations.dto.CompilationDto;
import ru.practicum.explorewithme.compilations.service.CompilationService;

@RestController
@RequiredArgsConstructor
public class CompilationController {

  private final CompilationService compilationService;

  @GetMapping("/compilations")
  public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
      @RequestParam(defaultValue = "0") int from,
      @RequestParam(defaultValue = "10") int size) {
    return compilationService.getCompilations(pinned, from, size);
  }

  @GetMapping("/compilations/{compilationId}")
  public CompilationDto getCompilation(@PathVariable long compilationId) {
    return compilationService.getCompilation(compilationId);
  }

  @PostMapping("/admin/compilations")
  @ResponseStatus(HttpStatus.CREATED)
  public CompilationDto createCompilation(@Valid @RequestBody CompilationCreationDto compilationCreationDto) {
    return compilationService.createCompilation(compilationCreationDto);
  }

  @DeleteMapping("/admin/compilations/{compilationId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCompilation(@PathVariable long compilationId) {
    compilationService.deleteCompilation(compilationId);
  }

  @PatchMapping("/admin/compilations/{compilationId}")
  public CompilationDto updateCompilation(@PathVariable long compilationId,
      @RequestBody CompilationCreationDto compilationCreationDto) {
    return compilationService.updateCompilation(compilationId, compilationCreationDto);
  }
}
