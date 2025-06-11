package com.app.distribution.movie_biz.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
/**
 * Annotation to check given account number is valid
 */
@Target({ ElementType.FIELD })
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AccountNumberValidator.class)
@Documented
public @interface IsValidAccountNumber {
	String message();

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}