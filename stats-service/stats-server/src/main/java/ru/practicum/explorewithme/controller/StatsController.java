package ru.practicum.explorewithme.controller;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.HitDto;
import ru.practicum.explorewithme.HitStatDto;
import ru.practicum.explorewithme.service.StatsService;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class StatsController {

  private final StatsService statsService;

  @PostMapping("/hit")
  @ResponseStatus(HttpStatus.CREATED)
  public void hit(@RequestBody HitDto hitDto) {
    log.info("controller");
    statsService.hit(hitDto);
  }

  @GetMapping("/stats")
  public HitStatDto[] getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
      @RequestParam(defaultValue = "") List<String> uris,
      @RequestParam(defaultValue = "false") Boolean unique) {
    return statsService.getStats(start, end, uris, unique);
  }
}
