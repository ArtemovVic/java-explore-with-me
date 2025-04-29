package ru.yandex.practicum.dao.mappers;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.dao.model.User;
import ru.yandex.practicum.dto.admin.user.AdminCreateUserDto;
import ru.yandex.practicum.dto.admin.user.AdminUserDto;


@UtilityClass
public class UserMapper {
    public static AdminUserDto toAdminDto(final User user) {
        return AdminUserDto
                .builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toEntity(final AdminCreateUserDto createUserDto) {
        return new User(
                null,
                createUserDto.getName(),
                createUserDto.getEmail()
        );
    }
}
