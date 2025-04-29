package ru.yandex.practicum.exception;

import jakarta.servlet.ServletException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            ValidationException.class,
            ServletException.class,
            InvalidArgumentException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorMessage handleValidationException(Exception e) {
        return buildMessage(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorMessage handleNotFoundException(NotFoundException e) {
        return buildMessage(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            DataErrorException.class,
            UniqueException.class,
            LogicErrorException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorMessage handleConflictException(RuntimeException e) {
        return buildMessage(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorMessage handleOtherError(Exception e) {
        return buildMessage(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ApiErrorMessage buildMessage(Throwable e, HttpStatus status) {
        log.error("Error: {}", e.getMessage(), e);
        return ApiErrorMessage
                .builder()
                .message(e.getMessage())
                .reason(status.getReasonPhrase())
                .errors(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList())
                .status(status.name())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
