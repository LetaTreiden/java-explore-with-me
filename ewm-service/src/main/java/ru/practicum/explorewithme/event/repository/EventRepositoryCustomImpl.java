package ru.practicum.explorewithme.event.repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.event.dto.EventStatus;
import ru.practicum.explorewithme.event.model.EventEntity;
import ru.practicum.explorewithme.request.model.RequestEntity;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventRepositoryCustomImpl implements EventRepositoryCustom {

  private final EntityManager em;

  @Override
  public List<EventEntity> searchEvents(List<Long> users, List<String> states, List<Integer> categories,
      String rangeStart, String rangeEnd, int from, int size) {
    var cb = em.getCriteriaBuilder();
    CriteriaQuery<EventEntity> cq = cb.createQuery(EventEntity.class);

    Root<EventEntity> statsEntityRoot = cq.from(EventEntity.class);
    List<Predicate> predicates = new ArrayList<>();

    var format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    if (rangeStart != null) {
      predicates.add(cb.greaterThan(statsEntityRoot.get("eventDate"),
          LocalDateTime.parse(rangeStart, format)));
    }
    if (rangeEnd != null) {
      predicates.add(cb.lessThan(statsEntityRoot.get("eventDate"),
          LocalDateTime.parse(rangeEnd, format)));
    }

    if (users != null && !users.isEmpty()) {
      predicates.add(cb.isTrue(statsEntityRoot.get("initiator").get("id").in(users)));
    }

    if (states != null && !states.isEmpty()) {
      var eStates = states.stream().map(EventStatus::valueOf).collect(Collectors.toList());
      predicates.add(cb.isTrue(statsEntityRoot.get("state").in(eStates)));
    }

    if (categories != null && !categories.isEmpty()) {
      predicates.add(cb.isTrue(statsEntityRoot.get("category").get("id").in(categories)));
    }

    cq.select(statsEntityRoot);
    cq.orderBy(cb.desc(cb.literal(1)));
    cq.where(predicates.toArray(new Predicate[0]));

    return em.createQuery(cq).setFirstResult(from).setMaxResults(size).getResultList();
  }

  @Override
  public List<EventEntity> searchEvents(String text, List<Integer> categories, Boolean paid, String rangeStart,
      String rangeEnd, Boolean onlyAvailable, String sort, int from, int size) {
    var cb = em.getCriteriaBuilder();
    CriteriaQuery<EventEntity> cq = cb.createQuery(EventEntity.class);

    Root<EventEntity> statsEntityRoot = cq.from(EventEntity.class);
    List<Predicate> predicates = new ArrayList<>();

    var format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    if (rangeStart != null) {
      predicates.add(cb.greaterThan(statsEntityRoot.get("eventDate"),
          LocalDateTime.parse(rangeStart, format)));
    }
    if (rangeEnd != null) {
      predicates.add(cb.lessThan(statsEntityRoot.get("eventDate"),
          LocalDateTime.parse(rangeEnd, format)));
    }

    if (text != null && !text.isBlank()) {
      var annotationLike = cb.like(statsEntityRoot.get("annotation"), "%" + text + "%");
      var descriptionLike = cb.like(statsEntityRoot.get("description"), "%" + text + "%");
      var orPredicate = cb.or(annotationLike, descriptionLike);
      predicates.add(orPredicate);
    }

    if (categories != null && !categories.isEmpty()) {
      predicates.add(cb.isTrue(statsEntityRoot.get("category").get("id").in(categories)));
    }

    if (paid != null) {
      predicates.add(cb.equal(statsEntityRoot.get("paid"), paid));
    }

    if (onlyAvailable != null && onlyAvailable) {
      Subquery<Long> sub = cq.subquery(Long.class);
      Root<RequestEntity> subRoot = sub.from(RequestEntity.class);
      sub.select(cb.count(subRoot.get("id")));
      sub.where(cb.equal(statsEntityRoot.get("id"), subRoot.get("event").get("id")));

      predicates.add(cb.greaterThan(statsEntityRoot.get("participantLimit"), sub));
    }

    if (sort != null) {
      cq.orderBy("EVENT_DATE".equals(sort)
          ? cb.desc(statsEntityRoot.get("eventDate"))
          : cb.desc(statsEntityRoot.get("views")));
    }

    cq.select(statsEntityRoot);
    cq.where(predicates.toArray(new Predicate[0]));

    return em.createQuery(cq).setFirstResult(from).setMaxResults(size).getResultList();
  }
}
