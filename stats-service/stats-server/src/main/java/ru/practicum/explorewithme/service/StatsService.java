package ru.practicum.explorewithme.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.HitDto;
import ru.practicum.explorewithme.HitStatDto;
import ru.practicum.explorewithme.model.StatsMapper;
import ru.practicum.explorewithme.repository.StatsRepository;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StatsService {

  private final StatsRepository statsRepository;

  public void hit(HitDto hitDto) {
    statsRepository.save(StatsMapper.toEntity(hitDto));
  }

  public List<HitStatDto> getStats(String start, String end, List<String> uris, Boolean unique) {
    return statsRepository.getStats(start, end, uris, unique);
  }
}
