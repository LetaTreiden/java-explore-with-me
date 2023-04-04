package ru.practicum.explorewithme.repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.HitStatDto;
import ru.practicum.explorewithme.model.StatsEntity;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StatsRepositoryCustomImpl implements StatsRepositoryCustom {

  private final EntityManager em;

  @Override
  public List<HitStatDto> getStats(String start, String end, List<String> uris, Boolean unique) {
    var cb = em.getCriteriaBuilder();
    CriteriaQuery<HitStatDto> cq = cb.createQuery(HitStatDto.class);

    Root<StatsEntity> statsEntityRoot = cq.from(StatsEntity.class);
    List<Predicate> predicates = new ArrayList<>();

    predicates.add(cb.between(statsEntityRoot.get("timestamp"), Timestamp.valueOf(start), Timestamp.valueOf(end)));

    if (uris != null && !uris.isEmpty()) {
      predicates.add(cb.isTrue(statsEntityRoot.get("uri").in(uris)));
    }

    cq.multiselect(statsEntityRoot.get("app"), statsEntityRoot.get("uri"),
        unique != null && unique
            ? cb.countDistinct(statsEntityRoot.get("ip"))
            : cb.count(statsEntityRoot.get("ip")));

    cq.groupBy(statsEntityRoot.get("uri"), statsEntityRoot.get("app"));
    cq.orderBy(cb.desc(cb.literal(3)));
    cq.where(predicates.toArray(new Predicate[0]));

    return em.createQuery(cq).getResultList();
  }
}
