package it.unina.dietiestates25.validation;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NullOrPastValidator 
        implements ConstraintValidator<NullOrPast, LocalDate> {
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return (value == null) || (value.isBefore(LocalDate.now()));
    }
}
