package ru.practicum.explorewithme;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
public class StatsClient extends BaseClient {

  @Autowired
  public StatsClient(@Value("${STAT_SERVER_URL}") String serverUrl, RestTemplateBuilder builder) {
    super(
        builder
            .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
            .requestFactory(HttpComponentsClientHttpRequestFactory::new)
            .build()
    );
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
