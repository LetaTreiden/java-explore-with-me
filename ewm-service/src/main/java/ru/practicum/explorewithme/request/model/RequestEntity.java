package ru.practicum.explorewithme.request.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.event.model.EventEntity;
import ru.practicum.explorewithme.request.dto.RequestStatusDto;
import ru.practicum.explorewithme.user.model.User;

@Getter
@Setter
@Builder
@Entity
@Table(name = "requests")
@NoArgsConstructor
@AllArgsConstructor
public class RequestEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  @JoinColumn(name = "event_id", referencedColumnName = "id", nullable = false)
  private EventEntity event;
  @ManyToOne
  @JoinColumn(name = "requester_id", referencedColumnName = "id", nullable = false)
  private User user;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RequestStatusDto status;
  @Column(nullable = false)
  private LocalDateTime createdOn;
}
