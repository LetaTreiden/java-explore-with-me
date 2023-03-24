package ru.practicum.explorewithme.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.HitDto;
import ru.practicum.explorewithme.HitStatDto;
import ru.practicum.explorewithme.HitToRepo;
import ru.practicum.explorewithme.model.StatsMapper;
import ru.practicum.explorewithme.repository.StatsRepository;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StatsService {

  private final StatsRepository statsRepository;
  private final StatsMapper mapper;

  public void hit(HitDto hitDto) {
    statsRepository.save(new StatsMapper().toStats(hitDto));
  }

  public HitStatDto[] getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
    List<HitToRepo> hits = statsRepository.getStats(start, end, uris, unique);
    return mapper.toListDtos(hits);
  }
}
