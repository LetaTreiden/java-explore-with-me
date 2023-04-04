package ru.practicum.explorewithme.request.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.request.model.RequestEntity;

public interface RequestRepository extends JpaRepository<RequestEntity, Long> {

  List<RequestEntity> findAllByUserId(long userId);

  List<RequestEntity> findAllByEventId(long eventId);

  boolean existsByEventIdAndUserId(long eventId, long userId);

  List<RequestEntity> findAllByIdIn(List<Long> eventIds);

  Long countByEventId(long eventId);
}
