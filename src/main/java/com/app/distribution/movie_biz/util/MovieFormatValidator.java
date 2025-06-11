package com.app.distribution.movie_biz.util;

import com.app.distribution.movie_biz.enums.MovieFormatEnum;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MovieFormatValidator implements ConstraintValidator<IsValidMovieFormat, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value != null && MovieFormatEnum.movieFormatValues().keySet().contains(value.toUpperCase());
	}
}