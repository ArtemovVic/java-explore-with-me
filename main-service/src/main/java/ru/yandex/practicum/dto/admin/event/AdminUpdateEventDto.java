package ru.yandex.practicum.dto.admin.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.dao.model.constant.AdminEventState;
import ru.yandex.practicum.dao.model.constant.Constant;
import ru.yandex.practicum.dto.event.UpdateEventDto;

import java.time.LocalDateTime;

@Data
public class AdminUpdateEventDto implements UpdateEventDto {
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

    private Integer participantLimit;

    private Boolean requestModeration;

    private AdminEventState stateAction;

    @Size(min = 3, max = 120)
    private String title;
}
