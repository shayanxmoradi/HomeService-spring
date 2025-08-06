package org.example.homeservice.dto.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ServiceValidatorImpl.class)
public @interface ServiceValidator {
    String message() default "Category services cannot have a price";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}