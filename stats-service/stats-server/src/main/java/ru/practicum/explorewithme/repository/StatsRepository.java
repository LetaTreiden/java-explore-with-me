package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.model.StatsEntity;

public interface StatsRepository extends JpaRepository<StatsEntity, Long>, StatsRepositoryCustom {

}
