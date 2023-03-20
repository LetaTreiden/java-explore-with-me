package ru.practicum.explorewithme;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class StatsClient extends BaseClient {

  public StatsClient(RestTemplate rest) {
    super(rest);
  }

  public ResponseEntity<Object> hit(HitDto hitDto) {
    return post("/hit", hitDto);
  }

  public ResponseEntity<Object> getStats(String start, String end, List<String> uris, Boolean unique) {
    return get("/stats", Map.of(
        "start", start,
        "end", end,
        "uris", uris,
        "unique", unique
    ));
  }
}
