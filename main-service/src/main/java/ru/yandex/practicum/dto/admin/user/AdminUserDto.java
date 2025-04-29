package ru.yandex.practicum.dto.admin.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminUserDto {
    private Long id;
    private String name;
    private String email;
}
