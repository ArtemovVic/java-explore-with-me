package ru.yandex.practicum.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.admin.compilation.AdminCreateCompilationDto;
import ru.yandex.practicum.dto.admin.compilation.AdminUpdateCompilationDto;
import ru.yandex.practicum.dto.compilation.CompilationDto;
import ru.yandex.practicum.service.util.CompilationService;

@RestController
@RequestMapping("/admin/compilations")
@Validated
@RequiredArgsConstructor
public class AdminCompilationsController {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(
            @RequestBody @Valid AdminCreateCompilationDto dto
    ) {
        return compilationService.createByAdmin(dto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long compId) {
        compilationService.deleteByAdmin(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto update(
            @PathVariable @Positive Long compId,
            @RequestBody @Valid AdminUpdateCompilationDto dto
    ) {
        return compilationService.updateByAdmin(compId, dto);
    }
}
