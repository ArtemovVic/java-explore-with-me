package ru.yandex.practicum.dto.admin.category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminCategoryDto {
    private Long id;
    private String name;
}
