package ru.practicum.explorewithme.event.repository;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.event.model.EventEntity;

public interface EventRepository extends JpaRepository<EventEntity, Long>, EventRepositoryCustom {

  List<EventEntity> findAllByInitiatorId(long initiatorId, PageRequest pageRequest);
}
