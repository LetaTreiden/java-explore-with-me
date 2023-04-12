package ru.practicum.explorewithme.event;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.event.dto.CommentDto;
import ru.practicum.explorewithme.event.dto.CommentDtoToCreate;
import ru.practicum.explorewithme.event.model.Comment;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.user.UserMapper;
import ru.practicum.explorewithme.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public static Comment toCommentFromCommentDtoCreate(CommentDtoToCreate commentDtoCreate, Event event, User author) {
        return new Comment(
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                event,
                author,
                commentDtoCreate.getText()
        );
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getCreated(),
                comment.getEvent().getId(),
                UserMapper.toUserComment(comment.getAuthor()),
                comment.getText()
        );
    }
}