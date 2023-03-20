package ru.practicum.explorewithme.model;

import java.sql.Timestamp;
import ru.practicum.explorewithme.HitDto;

public class StatsMapper {

  public Stats toStats(HitDto hitDto) {
    return Stats.builder()
        .app(hitDto.getApp())
        .uri(hitDto.getUri())
        .ip(hitDto.getIp())
        .timestamp(Timestamp.valueOf(hitDto.getTimestamp()))
        .build();
  }
}
