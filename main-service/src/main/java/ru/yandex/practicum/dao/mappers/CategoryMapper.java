package ru.yandex.practicum.dao.mappers;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.dao.model.Category;
import ru.yandex.practicum.dto.admin.category.AdminCategoryDto;
import ru.yandex.practicum.dto.admin.category.AdminCreateCategoryDto;
import ru.yandex.practicum.dto.category.CategoryDto;

@UtilityClass
public class CategoryMapper {

    public static AdminCategoryDto toAdminDto(final Category category) {
        return AdminCategoryDto
                .builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category toEntity(final AdminCreateCategoryDto createCategoryDto) {
        return new Category(
                null,
                createCategoryDto.getName()
        );
    }

    public static CategoryDto toDto(final Category category) {
        return CategoryDto
                .builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
