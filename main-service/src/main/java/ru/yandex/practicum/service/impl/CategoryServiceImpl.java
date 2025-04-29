package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dao.mappers.CategoryMapper;
import ru.yandex.practicum.dao.model.Category;
import ru.yandex.practicum.dto.admin.category.AdminCategoryDto;
import ru.yandex.practicum.dto.admin.category.AdminCreateCategoryDto;
import ru.yandex.practicum.dto.admin.category.AdminUpdateCategoryDto;
import ru.yandex.practicum.dto.category.CategoryDto;
import ru.yandex.practicum.exception.DataErrorException;
import ru.yandex.practicum.exception.LogicErrorException;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.exception.UniqueException;
import ru.yandex.practicum.repository.CategoryRepository;
import ru.yandex.practicum.repository.EventRepository;
import ru.yandex.practicum.service.CategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Transactional
    public AdminCategoryDto create(AdminCreateCategoryDto dto) {

        if (categoryRepository.existsByName(dto.getName())) {
            throw new UniqueException("Category with name " + dto.getName() + " already exists");
        }

        Category category = CategoryMapper.toEntity(dto);

        try {
            category = categoryRepository.save(category);
        } catch (DataErrorException e) {
            throw new DataErrorException("DB error: " + e.getMessage());
        }

        return CategoryMapper.toAdminDto(category);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("Category with id " + id + " does not exist");
        }

        if (eventRepository.existsByCategory_Id(id)) {
            throw new LogicErrorException("Category with id " + id + " has events");
        }

        try {
            categoryRepository.deleteById(id);
        } catch (DataErrorException e) {
            throw new DataErrorException("DB error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public AdminCategoryDto update(Long id, AdminUpdateCategoryDto dto) {

        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Category with id " + id + " does not exist")
        );

        if (categoryRepository.existsByNameAndIdNot(dto.getName(), id)) {
            throw new UniqueException("Category with name " + dto.getName() + " already exists");
        }

        category.setName(dto.getName());

        try {
            category = categoryRepository.save(category);
        } catch (DataErrorException e) {
            throw new DataErrorException("DB error: " + e.getMessage());
        }

        return CategoryMapper.toAdminDto(category);
    }

    @Override
    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Category with id " + id + " does not exist")
        );

        return CategoryMapper.toDto(category);
    }

    @Override
    public List<CategoryDto> getList(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);

        return categoryRepository.findAll(pageable).stream().map(CategoryMapper::toDto).toList();
    }
}
