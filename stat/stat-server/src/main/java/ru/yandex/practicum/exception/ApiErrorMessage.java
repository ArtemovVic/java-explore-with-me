package ru.yandex.practicum.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.dto.Constants;


import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ApiErrorMessage {

    private List<String> errors;

    private String message;

    private String reason;

    private String status;

    @DateTimeFormat(pattern = Constants.DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT)
    private LocalDateTime timestamp;
}
