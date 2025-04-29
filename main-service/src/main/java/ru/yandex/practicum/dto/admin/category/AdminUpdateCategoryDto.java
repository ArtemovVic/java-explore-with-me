package ru.yandex.practicum.dto.admin.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminUpdateCategoryDto {
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}
