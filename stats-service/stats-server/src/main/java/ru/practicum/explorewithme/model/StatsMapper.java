package ru.practicum.explorewithme.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.HitDto;
import ru.practicum.explorewithme.HitStatDto;
import ru.practicum.explorewithme.HitToRepo;

@Component
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

  public HitStatDto[] toListDtos(List<HitToRepo> hits) {
    List<HitStatDto> list = new ArrayList<>();
    for (HitToRepo hit : hits) {
      list.add(toDto(hit));
    }
    return list.toArray(new HitStatDto[0]);
  }
}
