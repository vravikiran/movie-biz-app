package com.app.distribution.movie_biz.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;

@Documented
@Constraint(validatedBy = MultiValueEnumValidatorImpl.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@NotNull(message = "Values cannot be empty")
public @interface MultiValueEnumValidator {
	Class<? extends Enum<?>> enumClazz();

	String message() default "Values are not valid";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}