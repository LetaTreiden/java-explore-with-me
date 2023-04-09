package ru.practicum.explorewithme.request.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.request.model.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {

  List<Request> findAllByUserId(long userId);

  List<Request> findAllByEventId(long eventId);

  boolean existsByEventIdAndUserId(long eventId, long userId);

  List<Request> findAllByIdIn(List<Long> list);

  @Query(value = "select * " +
          "from requests as r " +
          "WHERE r.event_id = ?1 " +
          "AND r.status='CONFIRMED' ",
          nativeQuery = true)
  List<Request> getRequestsEventConfirmed(int id);

  @Query(value = "select * " +
          "from requests as r " +
          "WHERE r.event_id = ?1 " +
          "AND r.status='CONFIRMED' ",
          nativeQuery = true)
  Map<Long, Integer> getRequestsEventsConfirmed(List<Long> eventIds);


 /* @Query(value = "select requests.event_id, count(requests) from requests as r " +
          "where r.event_id in ?1 and " +
          "r.status = ?2 " +
          "group by r.event_id")
  Map<Long, Integer> findConfirmedRequestsForEvents(List<Long> eventIds, RequestStatus state);

  */
}
