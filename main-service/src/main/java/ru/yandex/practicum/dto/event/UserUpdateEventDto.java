package ru.yandex.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import ru.yandex.practicum.dao.model.constant.Constant;
import ru.yandex.practicum.dao.model.constant.UserEventState;

import java.time.LocalDateTime;

@Data
public class UserUpdateEventDto implements UpdateEventDto {

    @Size(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000)
    private String description;

    @DateTimeFormat(pattern = Constant.DATE_TIME_FORMAT)
    @JsonFormat(pattern = Constant.DATE_TIME_FORMAT, shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    @PositiveOrZero
    @Nullable
    private Integer participantLimit;

    private Boolean requestModeration;

    private UserEventState stateAction;

    @Size(min = 3, max = 120)
    private String title;
}
