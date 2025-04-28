package ru.yandex.practicum.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.admin.category.AdminCategoryDto;
import ru.yandex.practicum.dto.admin.category.AdminCreateCategoryDto;
import ru.yandex.practicum.dto.admin.category.AdminUpdateCategoryDto;
import ru.yandex.practicum.service.util.CategoryService;

@RestController
@RequestMapping("/admin/categories")
@Validated
@RequiredArgsConstructor
public class AdminCategoriesController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdminCategoryDto create(
            @RequestBody @Valid AdminCreateCategoryDto dto
    ) {
        return categoryService.create(dto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long catId) {
        categoryService.delete(catId);
    }

    @PatchMapping("/{catId}")
    public AdminCategoryDto update(
            @PathVariable @Positive Long catId,
            @RequestBody @Valid AdminUpdateCategoryDto dto
    ) {
        return categoryService.update(catId, dto);
    }
}
