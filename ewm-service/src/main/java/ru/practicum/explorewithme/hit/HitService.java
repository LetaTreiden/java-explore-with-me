package ru.practicum.explorewithme.hit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.HitStatDto;
import ru.practicum.explorewithme.StatsClient;
import ru.practicum.explorewithme.event.model.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class HitService {


    static final String URI = "/events/";

    static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

     private final StatsClient client;


    public Map<Long, Long> get(List<Event> events) {
        List<String> uris = new ArrayList<>();
        for (Event event: events) {
            uris.add(URI + event.getId().toString());
        }
        log.info("uris {}", uris);
        List<HitStatDto> stats = client.get(LocalDateTime.now().format(DATE_TIME_FORMATTER),
                LocalDateTime.now().format(DATE_TIME_FORMATTER), uris, true);
        log.info("stats {}", stats);
        Map<Long, Long> hits = new HashMap<>();
        for (HitStatDto viewStatsDto: stats) {
            Long id = Long.parseLong(viewStatsDto.getUri().split("/")[2]);
            log.info(id.toString());
            hits.put(id, viewStatsDto.getHits());
        }
        return hits;
    }

    public Map<Long, Long> getTwo(List<Event> events) {
        List<String> uris = new ArrayList<>();
        for (Event event: events) {
            uris.add(URI + event.getId().toString());
        }
        log.info("uris {}", uris);
        List<HitStatDto> stats = client.get(LocalDateTime.now().format(DATE_TIME_FORMATTER),
                LocalDateTime.now().format(DATE_TIME_FORMATTER), uris, true);
        log.info("stats {}", stats);
        Map<Long, Long> hits = new HashMap<>();
        for (HitStatDto viewStatsDto: stats) {
            Long id = Long.parseLong(viewStatsDto.getUri().split("/")[1]);
            log.info(id.toString());
            hits.put(id, viewStatsDto.getHits());
        }
        return hits;
    }

}
