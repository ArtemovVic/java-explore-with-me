package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.yandex.practicum.dto.RequestCreateHitDto;
import ru.yandex.practicum.dto.StatGetRequestDto;
import ru.yandex.practicum.dto.StatHitDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class StatsClient {

    private static final String API_HIT = "/hit";
    private static final String API_STATS = "/stats";

    private final WebClient client;

    public void saveHit(RequestCreateHitDto dto) {
        client.post()
                .uri(API_HIT)
                .bodyValue(dto)
                .retrieve()
                .toBodilessEntity()
                .doOnError(e -> log.error("Failed to send hit to stats-server", e))
                .onErrorComplete()
                .block();
    }

    public List<StatHitDto> getStats(StatGetRequestDto dto) {
        Optional<String> urisOptional = dto.getUris() != null && !dto.getUris().isEmpty()
                ? Optional.of(String.join(",", dto.getUris()))
                : Optional.empty();


        List<StatHitDto> response = client
                .get()
                .uri(
                        builder -> builder.path(API_STATS)
                                .queryParam("start", dto.getStart())
                                .queryParam("end", dto.getEnd())
                                .queryParam("unique", dto.isUnique())
                                .queryParamIfPresent("uris", urisOptional)
                                .build()
                )
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<StatHitDto>>() {})
                .doOnError(e -> {
                    log.error("Failed to getting stats", e);
                    log.error("dto {}", dto);
                })
                .onErrorReturn(Collections.emptyList())
                .block();

        return response == null
                ? Collections.emptyList()
                : response;
    }
}
