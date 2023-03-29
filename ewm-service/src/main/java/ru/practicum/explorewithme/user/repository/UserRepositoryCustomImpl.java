package ru.practicum.explorewithme.user.repository;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.user.model.User;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

  private final EntityManager em;

  @Override
  public List<UserDto> getUsers(List<Long> ids, int from, int size) {
    var cb = em.getCriteriaBuilder();
    CriteriaQuery<UserDto> cq = cb.createQuery(UserDto.class);

    Root<User> statsEntityRoot = cq.from(User.class);
    List<Predicate> predicates = new ArrayList<>();

    if (ids != null && !ids.isEmpty()) {
      predicates.add(cb.isTrue(statsEntityRoot.get("id").in(ids)));
    }

    cq.multiselect(statsEntityRoot.get("id"), statsEntityRoot.get("name"), statsEntityRoot.get("email"));

    cq.orderBy(cb.desc(cb.literal(1)));
    cq.where(predicates.toArray(new Predicate[0]));

    return em.createQuery(cq).setFirstResult(from).setMaxResults(size).getResultList();
  }
}
