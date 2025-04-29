package ru.yandex.practicum.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.dao.model.DateRange;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class DateRangeValidator implements ConstraintValidator<DateRange, Object> {
    private String rangeStart;
    private String rangeEnd;

    @Override
    public void initialize(DateRange annotation) {
        rangeStart = annotation.rangeStart();
        rangeEnd = annotation.rangeEnd();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        try {
            final Field rangeStartField = value.getClass().getDeclaredField(rangeStart);
            rangeStartField.setAccessible(true);

            final Field rangeEndField = value.getClass().getDeclaredField(rangeEnd);
            rangeEndField.setAccessible(true);

            final LocalDateTime rangeStartLocalDateTime = (LocalDateTime) rangeStartField.get(value);
            final LocalDateTime rangeEndLocalDateTime = (LocalDateTime) rangeEndField.get(value);

            if (rangeEndLocalDateTime == null || rangeStartLocalDateTime == null) {
                return true;
            }

            return rangeStartLocalDateTime.isBefore(rangeEndLocalDateTime);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
            return false;
        }
    }
}
