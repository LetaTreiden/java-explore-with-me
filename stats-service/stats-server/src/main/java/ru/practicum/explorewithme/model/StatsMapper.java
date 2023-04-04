package ru.practicum.explorewithme.model;

import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.HitDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatsMapper {

  public static StatsEntity toEntity(HitDto hitDto) {
    return StatsEntity.builder()
        .app(hitDto.getApp())
        .uri(hitDto.getUri())
        .ip(hitDto.getIp())
        .timestamp(Timestamp.valueOf(hitDto.getTimestamp()))
        .build();
  }
}
