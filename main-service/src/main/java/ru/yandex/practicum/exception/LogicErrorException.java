package ru.yandex.practicum.exception;

public class LogicErrorException extends RuntimeException {
    public LogicErrorException(String message) {
        super(message);
    }
}
