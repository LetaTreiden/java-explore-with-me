package ru.practicum.explorewithme.repository;

import java.util.List;
import ru.practicum.explorewithme.HitStatDto;

public interface StatsRepositoryCustom {

  List<HitStatDto> getStats(String start, String end, List<String> uris, Boolean unique);
}
