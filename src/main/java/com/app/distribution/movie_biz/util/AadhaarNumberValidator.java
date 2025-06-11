package com.app.distribution.movie_biz.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
/**
 * Checks given account number is valid
 */
public class AadhaarNumberValidator  implements ConstraintValidator<IsValidAadhaarNumber, Long>{

	@Override
	public boolean isValid(Long value, ConstraintValidatorContext context) {
		return (value == null || (value != null && Long.toString(value).length() != 12)) ? false : true;
	}

}