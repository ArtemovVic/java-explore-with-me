package ru.yandex.practicum.dto.event;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.yandex.practicum.dao.model.constant.EventRequestState;

import java.util.List;

@Data
public class UpdateEventRequestsDto {
    @NotNull
    @NotEmpty
    private List<Long> requestIds;

    @NotNull
    private EventRequestState status;
}
