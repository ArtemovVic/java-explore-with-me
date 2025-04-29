package ru.yandex.practicum.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventRequestResultDto {
    private List<EventRequestDto> confirmedRequests;
    private List<EventRequestDto> rejectedRequests;
}
