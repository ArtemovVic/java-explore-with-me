package ru.yandex.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;


@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> unsupportedEncodingHandle(RuntimeException e) {
        log.debug("RuntimeException: {}", e.getMessage());

        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
            InvalidArgumentException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorMessage handleValidationException(Exception e) {
        return buildMessage(e, HttpStatus.BAD_REQUEST);
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
