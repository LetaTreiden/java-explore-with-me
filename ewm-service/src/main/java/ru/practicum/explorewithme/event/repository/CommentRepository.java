package ru.practicum.explorewithme.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.event.model.Comment;

import java.util.List;
import java.util.Map;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByEventId(Long id, Pageable page);

    @Query(value = "select * " +
            "from comments as c " +
            "WHERE c.event_id = ?1 ",
            nativeQuery = true)
    Map<Long, Integer> getCommentsEvents(List<Long> eventIds);

}
