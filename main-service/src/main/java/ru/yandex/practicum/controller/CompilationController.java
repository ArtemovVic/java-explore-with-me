package ru.yandex.practicum.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dao.model.constant.Constant;
import ru.yandex.practicum.dto.compilation.CompilationDto;
import ru.yandex.practicum.service.CompilationService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@Validated
@RequiredArgsConstructor
public class CompilationController {

    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getList(
            @RequestParam(defaultValue = "false") boolean pinned,
            @RequestParam(defaultValue = Constant.INT_MIN_STRING) @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size
    ) {
        return compilationService.getList(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getById(@PathVariable Long compId) {
        return compilationService.getById(compId);
    }
}
