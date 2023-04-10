package ru.practicum.explorewithme.event.repository;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.event.dto.EventInfo;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventStatus;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {
  List<Event> findAllByInitiatorId(long initiatorId, PageRequest pageRequest);
}
