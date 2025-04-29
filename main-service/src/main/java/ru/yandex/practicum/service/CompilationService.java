package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.admin.compilation.AdminCreateCompilationDto;
import ru.yandex.practicum.dto.admin.compilation.AdminUpdateCompilationDto;
import ru.yandex.practicum.dto.compilation.CompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getList(boolean pinned, int from, int size);

    CompilationDto getById(Long id);

    CompilationDto createByAdmin(AdminCreateCompilationDto dto);

    void deleteByAdmin(Long id);

    CompilationDto updateByAdmin(Long id, AdminUpdateCompilationDto dto);
}
