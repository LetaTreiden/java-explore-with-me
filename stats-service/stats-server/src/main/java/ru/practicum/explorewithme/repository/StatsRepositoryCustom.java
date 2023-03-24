package ru.practicum.explorewithme.repository;

import java.time.LocalDateTime;
import java.util.List;
import ru.practicum.explorewithme.HitToRepo;

public interface StatsRepositoryCustom {

  List<HitToRepo> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
