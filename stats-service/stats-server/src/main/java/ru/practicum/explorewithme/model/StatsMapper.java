package ru.practicum.explorewithme.model;

import java.sql.Timestamp;
import ru.practicum.explorewithme.HitDto;
import ru.practicum.explorewithme.HitStatDto;
import ru.practicum.explorewithme.HitToRepo;

public class StatsMapper {

  public Stats toStats(HitDto hitDto) {
    return Stats.builder()
        .app(hitDto.getApp())
        .uri(hitDto.getUri())
        .ip(hitDto.getIp())
        .timestamp(Timestamp.valueOf(hitDto.getTimestamp()))
        .build();
  }

  public HitStatDto toDto(HitToRepo hit) {
    HitStatDto hitDto = new HitStatDto();
    hitDto.setHits(hit.getHits());
    hitDto.setApp(hit.getApp());
    hitDto.setUri(hit.getUri());
    return hitDto;
  }
}
