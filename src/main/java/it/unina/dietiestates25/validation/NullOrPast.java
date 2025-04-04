package it.unina.dietiestates25.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NullOrPastValidator.class)
public @interface NullOrPast {
    String message() default "If present, must be a past date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
