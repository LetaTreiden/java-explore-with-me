package ru.practicum.explorewithme.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.event.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByEventId(Long id, Pageable page);

    List<Comment> findAllByEventId(Long id);

}
