package com.app.distribution.movie_biz.util;

import com.app.distribution.movie_biz.enums.CbfcRatingEnum;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CbfcRatingValidator implements ConstraintValidator<IsValidCbfcRating, String>{

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value != null && CbfcRatingEnum.cbfcValues().keySet().contains(value.toUpperCase());
	}
}