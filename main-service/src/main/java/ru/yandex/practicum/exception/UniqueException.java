package ru.yandex.practicum.exception;

public class UniqueException extends RuntimeException {
    public UniqueException(String message) {
        super(message);
    }
}
