package ru.yandex.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.dao.model.constant.Constant;

import java.time.LocalDateTime;

@Data
@Builder
public class EventDto {
    private Long id;

    private String annotation;

    private CategoryDto category;

    private int confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.DATE_TIME_FORMAT)
    private LocalDateTime createdOn;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.DATE_TIME_FORMAT)
    private LocalDateTime eventDate;

    private InitiatorDto initiator;

    private LocationDto location;

    private boolean paid;

    private int participantLimit;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.DATE_TIME_FORMAT)
    private LocalDateTime publishedOn;

    private boolean requestModeration;

    private String state;

    private String title;

    private int views;

    public record CategoryDto(Long id, String name) {}

    public record InitiatorDto(Long id, String name) {}

    public record LocationDto(float lat, float lon) {}
}
