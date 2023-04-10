package ru.practicum.explorewithme.event.repository;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.event.model.Event;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {
  List<Event> findAllByInitiatorId(long initiatorId, PageRequest pageRequest);
}
