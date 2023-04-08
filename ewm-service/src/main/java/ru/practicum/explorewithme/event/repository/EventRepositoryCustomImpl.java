package ru.practicum.explorewithme.event.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventStatus;
import ru.practicum.explorewithme.request.model.Request;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventRepositoryCustomImpl implements EventRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Event> searchEvents(List<Long> users, List<String> states, List<Integer> categories,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        var cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);

        Root<Event> statsEntityRoot = cq.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();

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
    public List<Event> searchEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, int from, int size) {
        var cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);

        Root<Event> statsEntityRoot = cq.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();

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
            Root<Request> subRoot = sub.from(Request.class);
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
