package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.model.Stats;

public interface StatsRepository extends JpaRepository<Stats, Long>, StatsRepositoryCustom {

}
