package ru.yandex.practicum.service.util;

import ru.yandex.practicum.dto.admin.user.AdminCreateUserDto;
import ru.yandex.practicum.dto.admin.user.AdminUserDto;

import java.util.List;

public interface UserService {
    List<AdminUserDto> getList(int from, int size, List<Long> ids);

    AdminUserDto create(AdminCreateUserDto dto);

    void delete(Long id);
}
