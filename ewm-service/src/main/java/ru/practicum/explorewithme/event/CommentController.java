package ru.practicum.explorewithme.event;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.dto.CommentDto;
import ru.practicum.explorewithme.event.dto.CommentDtoToCreate;
import ru.practicum.explorewithme.event.dto.CommentDtoToUpdate;
import ru.practicum.explorewithme.event.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/comments")
public class CommentController {
    private final CommentService service;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@Valid @RequestBody CommentDtoToCreate commentDtoCreate,
                                    @RequestParam Long eventId,
                                    @RequestParam Long userId) {
        return service.create(commentDtoCreate, eventId, userId);
    }

    @GetMapping("/{commentId}")
    public CommentDto findById(@PathVariable Long commentId) {
        return service.getById(commentId);
    }

    @GetMapping()
    public List<CommentDto> findAllForEvent(@RequestParam Long eventId, @RequestParam Long userId,
                                                    @PositiveOrZero @RequestParam(defaultValue = "1") int from,
                                                    @Positive @RequestParam(defaultValue = "10") int size) {
        PageRequest page = PageRequest.of(from / size, size);
        return service.findAllForEvent(eventId, page, userId);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long commentId,
                                    @RequestParam Long userId) {
        service.delete(commentId,userId);
    }


    @DeleteMapping("/admin/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByAdmin(@PathVariable Long commentId) {
        service.deleteByAdmin(commentId);
    }



    @PatchMapping("/{commentId}")
    public CommentDto update(@PathVariable Long commentId,
                                  @RequestParam Long userId,
                                  @Valid @RequestBody CommentDtoToUpdate commentDtoUpdate) {
        return service.update(commentId, userId, commentDtoUpdate);
    }
}
