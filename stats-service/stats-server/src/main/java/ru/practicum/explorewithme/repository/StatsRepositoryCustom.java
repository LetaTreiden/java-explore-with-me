package ru.practicum.explorewithme.repository;

import java.time.LocalDateTime;
import java.util.List;
import ru.practicum.explorewithme.HitStatDto;

public interface StatsRepositoryCustom {

  List<HitStatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
