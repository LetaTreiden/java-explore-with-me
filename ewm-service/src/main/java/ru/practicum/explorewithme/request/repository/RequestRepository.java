package ru.practicum.explorewithme.request.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.request.model.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {

  List<Request> findAllByUserId(long userId);

  List<Request> findAllByEventId(long eventId);

  boolean existsByEventIdAndUserId(long eventId, long userId);

  List<Request> findAllByIdIn(List<Long> eventIds);

  Long countByEventId(long eventId);
}
