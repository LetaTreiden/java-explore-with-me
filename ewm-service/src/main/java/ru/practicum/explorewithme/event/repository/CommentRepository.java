package ru.practicum.explorewithme.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.event.model.Comment;

import java.util.List;
import java.util.Map;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByEventId(Long id, Pageable page);

    @Query(value = "select com.event.id, count(com) from Comment com where com.event.id = ?1 group by com.event.id")
    Map<Long, Integer> getCommentEvents(List<Long> eventIds);

}
