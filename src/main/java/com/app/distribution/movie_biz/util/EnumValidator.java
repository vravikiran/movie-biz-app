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
@Constraint(validatedBy = EnumValidatorImpl.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@NotNull(message = "Value cannot be null")
public @interface EnumValidator {
	Class<? extends Enum<?>> enumClazz();

	String message() default "Value is not valid";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}