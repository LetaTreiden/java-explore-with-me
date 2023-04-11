package ru.practicum.explorewithme.event.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.event.CommentMapper;
import ru.practicum.explorewithme.event.dto.CommentDto;
import ru.practicum.explorewithme.event.dto.CommentDtoToCreate;
import ru.practicum.explorewithme.event.dto.CommentDtoToUpdate;
import ru.practicum.explorewithme.event.model.Comment;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.repository.CommentRepository;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.exceptions.ValidationException;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImpl implements CommentService {

    CommentRepository commentRepo;
    EventRepository eventRepo;
    UserRepository userRepo;

    @Override
    @Transactional
    public CommentDto create(CommentDtoToCreate commentDtoCreate, Long eventId, Long userId) {
        log.info("create");
        User user = checkUser(userId);
        Event event = checkEvent(eventId);
        Comment comment = CommentMapper.toCommentFromCommentDtoCreate(commentDtoCreate, event, user);
        log.info("id{}", comment.getId());
        commentRepo.save(comment);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public CommentDto getById(Long commentId) {
        log.info("getById");
        return CommentMapper.toCommentDto(checkComment(commentId));
    }

    @Override
    @Transactional
    public void delete(Long commentId, Long userId) {
        log.info("delete");
        Comment comment = checkComment(commentId);
        checkUser(userId);
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ValidationException("You don't have such rights");
        }
        commentRepo.deleteById(commentId);
    }

    @Override
    @Transactional
    public CommentDto update(Long commentId, Long userId, CommentDtoToUpdate commentDtoUpdate) {
        log.info("update");
        Comment comment = checkComment(commentId);
        checkUser(userId);
        if (!userId.equals(comment.getAuthor().getId())) {
            throw new ValidationException("You don't have such rights");
        }

        if (commentDtoUpdate.getText() != null) {
            comment.setText(commentDtoUpdate.getText());
        }
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public List<CommentDto> findAllForEvent(Long eventId, Pageable page) {
        log.info("find all");
        checkEvent(eventId);
        return commentRepo.findAllByEventId(eventId, page).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteByAdmin(Long commentId) {
        log.info("delete by Admin");
        checkComment(commentId);
        commentRepo.deleteById(commentId);
    }

    private Comment checkComment(Long commentId) {
        return commentRepo.findById(commentId).orElseThrow(() -> {
            throw new NoSuchElementException("There is no such comment id" + commentId);
        });
    }

    private Event checkEvent(Long eventId) {
        Event event = eventRepo.findById(eventId).orElseThrow(() -> {
            throw new NoSuchElementException("There is no such event id" + eventId);
        });
        return event;
    }

    private User checkUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> {
            throw new NoSuchElementException("There is no such user id" + userId);
        });
        return user;
    }
}
