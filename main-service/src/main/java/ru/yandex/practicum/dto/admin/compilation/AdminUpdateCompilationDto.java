package ru.yandex.practicum.dto.admin.compilation;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.Set;

@Data
public class AdminUpdateCompilationDto {
    @Nullable
    @Size(min = 1, max = 50)
    private String title;

    private Boolean pinned;

    private Set<Long> events;
}
