package ru.yandex.practicum.exception;

public class DataErrorException extends RuntimeException {
    public DataErrorException(String message) {
        super(message);
    }
}
