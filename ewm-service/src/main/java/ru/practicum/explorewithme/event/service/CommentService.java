package ru.practicum.explorewithme.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.event.dto.CommentDto;
import ru.practicum.explorewithme.event.dto.CommentDtoToCreate;
import ru.practicum.explorewithme.event.dto.CommentDtoToUpdate;

import java.util.List;

public interface CommentService {
    CommentDto create(CommentDtoToCreate commentDtoCreate, Long eventId, Long userId);

    CommentDto getById(Long commentId);

    void delete(Long commentId, Long userId);

    CommentDto update(Long commentId, Long userId, CommentDtoToUpdate commentDtoUpdate);

    List<CommentDto> findAllForEvent(Long eventId, Pageable page);

    void deleteByAdmin(Long commentId);

}
