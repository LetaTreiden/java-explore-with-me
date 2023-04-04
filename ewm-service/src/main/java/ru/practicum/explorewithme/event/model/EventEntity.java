package ru.practicum.explorewithme.event.model;

import java.time.LocalDateTime;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
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
import ru.practicum.explorewithme.category.model.CategoryEntity;
import ru.practicum.explorewithme.event.dto.EventStatus;
import ru.practicum.explorewithme.event.dto.LocationDto;
import ru.practicum.explorewithme.user.model.User;

@Getter
@Setter
@Entity
@Builder
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
public class EventEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  @JoinColumn(name = "initiator_id", referencedColumnName = "id", nullable = false)
  private User initiator;
  @Column(nullable = false)
  private String annotation;
  @ManyToOne
  @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
  private CategoryEntity category;
  private String description;
  @Column(nullable = false)
  private LocalDateTime eventDate;
  @Embedded
  @AttributeOverride(name = "lat", column = @Column(name = "lat", nullable = false))
  @AttributeOverride(name = "lon", column = @Column(name = "lon", nullable = false))
  private LocationDto location;
  private Boolean paid;
  private Integer participantLimit;
  private Boolean requestModeration;
  @Column(nullable = false)
  private String title;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EventStatus state;
  @Column(nullable = false)
  private LocalDateTime createdOn;
  private LocalDateTime publishedOn;
  private Integer confirmedRequests;
  private Long views;

  public EventEntity(Long id) {
    this.id = id;
  }
}
