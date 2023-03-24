package ru.practicum.explorewithme.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.HitDto;
import ru.practicum.explorewithme.HitStatDto;
import ru.practicum.explorewithme.service.StatsService;

@RestController
@Validated
@RequiredArgsConstructor
public class StatsController {

  private final StatsService statsService;

  @PostMapping("/hit")
  @ResponseStatus(HttpStatus.CREATED)
  public void hit(@RequestBody HitDto hitDto) {
    statsService.hit(hitDto);
  }

  @GetMapping("/stats")
  public List<HitStatDto> getStats(@RequestParam String start,
      @RequestParam String end,
      @RequestParam(required = false) List<String> uris,
      @RequestParam(required = false) Boolean unique) {
    return statsService.getStats(start, end, uris, unique);
  }
}
