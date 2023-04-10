package ru.practicum.explorewithme;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StatsClient extends BaseClient {

    private final String appName;
    private final String serverUrl;


    @Autowired
    public StatsClient(@Value("${stat-server.url}") String serverUrl, @Value("${app.name}") String appName,
                       RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
        this.serverUrl = serverUrl;
        this.appName = appName;
    }

    public ResponseEntity<Object> hit(HttpServletRequest request) {
        log.info("fine");
        HitDto hitDto = new HitDto(appName, request.getRequestURI(),
                request.getRemoteAddr(), LocalDateTime.now(), 0L);
        return post("/hit", hitDto);
    }

    public ResponseEntity<Object> getHits(String start, String end, String[] uri, boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uri", uri,
                "unique", unique
        );
        return get("/stats?start={start}&end={end}&uri={{uri}}&unique={unique}", parameters);
    }

    public ResponseEntity<Object> getStats(String start, String end, List<String> uris, Boolean unique) {
        return get("/stats", Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        ));
    }

     public List<HitStatDto> get(String start, String end, List<String> uris, Boolean unique) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start);
        parameters.put("end", end);
        if (!uris.isEmpty()) {
            parameters.put("uri", uris);
        }
        parameters.put("unique", unique);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        log.info(serverUrl);
        ResponseEntity<Object> response = rest
                .exchange(serverUrl + "/stats?start={start}&end={end}&uri={{uri}}&unique={unique}",
                        HttpMethod.GET, requestEntity, Object.class, parameters);
        List<HitStatDto> result = objectMapper.convertValue(response.getBody(), new TypeReference<>(){});
        if (result == null) {
            return List.of();
        } else {
            return result;
        }
    }



}
