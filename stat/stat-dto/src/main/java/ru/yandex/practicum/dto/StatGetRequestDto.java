package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;


import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatGetRequestDto {

    @NotNull
    @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT)
    private LocalDateTime start;

    @NotNull
    @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT)
    private LocalDateTime end;

    @NotNull
    private Set<String> uris;

    private boolean unique;
}
