package org.example.identityservice.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.identityservice.validator.impl.UniqueImpl;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueImpl.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Unique {
    String message() default "Giá trị đã tồn tại";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<?> entity();

    String fieldName();
}
