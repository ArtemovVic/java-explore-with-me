package ru.yandex.practicum.dao.model;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.yandex.practicum.validator.DateRangeValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateRangeValidator.class)
public @interface DateRange {
    String message() default "Invalid date range";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String rangeStart() default "";

    String rangeEnd() default "";
}
