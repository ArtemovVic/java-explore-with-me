package ru.yandex.practicum.dto.admin.event;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.dao.model.DateRange;
import ru.yandex.practicum.dao.model.constant.Constant;
import ru.yandex.practicum.dao.model.constant.EventState;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DateRange(
        rangeStart = "rangeStart",
        rangeEnd = "rangeEnd"
)
public class AdminSearchEventDto {
    private Set<Long> users;

    private Set<EventState> states;

    private Set<Long> categories;

    @DateTimeFormat(pattern = Constant.DATE_TIME_FORMAT)
    private LocalDateTime rangeStart;

    @DateTimeFormat(pattern = Constant.DATE_TIME_FORMAT)
    private LocalDateTime rangeEnd;

    @PositiveOrZero
    private int from = 0;

    @Positive
    private int size = 10;
}
