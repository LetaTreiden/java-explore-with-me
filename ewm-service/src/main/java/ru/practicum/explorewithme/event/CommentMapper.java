package ru.practicum.explorewithme.event;

import ru.practicum.explorewithme.event.dto.CommentDto;
import ru.practicum.explorewithme.event.dto.CommentDtoToCreate;
import ru.practicum.explorewithme.event.model.Comment;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.user.UserMapper;
import ru.practicum.explorewithme.user.model.User;

public class CommentMapper {

    public static Comment toComment(CommentDto commentDto, Event event) {
        return new Comment(
                commentDto.getId(),
                commentDto.getCreated(),
                event,
                UserMapper.toUser(commentDto.getAuthor()),
                commentDto.getText()
        );
    }

    public static Comment toCommentFromCommentDtoCreate(CommentDtoToCreate commentDtoCreate, Event event, User author) {
        return new Comment(
                null,
                commentDtoCreate.getCreated(),
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