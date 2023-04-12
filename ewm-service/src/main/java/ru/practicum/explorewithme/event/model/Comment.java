package ru.practicum.explorewithme.event.model;

import lombok.*;
import ru.practicum.explorewithme.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@Table(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private LocalDateTime created;

    private LocalDateTime updated;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @NotBlank
    private String text;
}
