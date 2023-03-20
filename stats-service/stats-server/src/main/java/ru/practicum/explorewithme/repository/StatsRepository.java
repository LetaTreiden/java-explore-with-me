package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.HitStatDto;
import ru.practicum.explorewithme.model.Stats;

import java.util.List;

public interface StatsRepository extends JpaRepository<Stats, Long> {
    List<HitStatDto> getStats(String start, String end, List<String> uris, Boolean unique);
}
