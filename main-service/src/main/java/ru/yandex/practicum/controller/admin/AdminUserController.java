package ru.yandex.practicum.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dao.model.constant.Constant;
import ru.yandex.practicum.dto.admin.user.AdminCreateUserDto;
import ru.yandex.practicum.dto.admin.user.AdminUserDto;
import ru.yandex.practicum.service.UserService;


import java.util.List;

@RestController
@RequestMapping("/admin/users")
@Validated
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;

    @GetMapping
    public List<AdminUserDto> getList(
            @RequestParam(value = "ids", required = false) List<Long> ids,
            @RequestParam(value = "from", defaultValue = Constant.INT_MIN_STRING) @PositiveOrZero int from,
            @RequestParam(value = "size", defaultValue = "10") @Positive int size
    ) {
        return userService.getList(from, size, ids);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdminUserDto create(
            @RequestBody @Valid AdminCreateUserDto dto
    ) {
        return userService.create(dto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long userId) {
        userService.delete(userId);
    }
}
