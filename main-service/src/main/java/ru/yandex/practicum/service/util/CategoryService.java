package ru.yandex.practicum.service.util;

import ru.yandex.practicum.dto.admin.category.AdminCategoryDto;
import ru.yandex.practicum.dto.admin.category.AdminCreateCategoryDto;
import ru.yandex.practicum.dto.admin.category.AdminUpdateCategoryDto;
import ru.yandex.practicum.dto.category.CategoryDto;

import java.util.List;

public interface CategoryService {
    AdminCategoryDto create(AdminCreateCategoryDto dto);

    void delete(Long id);

    AdminCategoryDto update(Long id, AdminUpdateCategoryDto dto);

    CategoryDto getById(Long id);

    List<CategoryDto> getList(int from, int size);
}
