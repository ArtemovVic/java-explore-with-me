package ru.yandex.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.dao.model.constant.Constant;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateEventDto {

    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

    @NotNull
    @Positive
    private Long category;

    @NotNull
    @DateTimeFormat(pattern = Constant.DATE_TIME_FORMAT)
    @JsonFormat(pattern = Constant.DATE_TIME_FORMAT, shape = JsonFormat.Shape.STRING)
    @FutureOrPresent
    private LocalDateTime eventDate;

    private boolean paid = false;

    private boolean requestModeration = true;

    @PositiveOrZero
    private int participantLimit = 0;

    @NotNull
    private LocationDto location;

    public record LocationDto(float lat, float lon) {
    }
}
