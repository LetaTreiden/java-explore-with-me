package ru.practicum.explorewithme.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.HitDto;
import ru.practicum.explorewithme.HitStatDto;
import ru.practicum.explorewithme.HitToRepo;
import ru.practicum.explorewithme.model.Stats;
import ru.practicum.explorewithme.model.StatsMapper;
import ru.practicum.explorewithme.repository.StatsRepository;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class StatsService {

  private final StatsRepository statsRepository;
  private final StatsMapper statsMapper;

  public void hit(HitDto hitDto) {
    log.info("service");
    Stats stats = statsMapper.toStats(hitDto);
    log.info("stats done");
    statsRepository.save(stats);
    log.info(stats.getId().toString());
  }

  public HitStatDto[] getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
    List<HitToRepo> hits = statsRepository.getStats(start, end, uris, unique);
    return statsMapper.toListDtos(hits);
  }
}
