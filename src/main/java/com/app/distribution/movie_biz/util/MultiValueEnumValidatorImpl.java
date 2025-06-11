package com.app.distribution.movie_biz.util;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MultiValueEnumValidatorImpl implements ConstraintValidator<MultiValueEnumValidator, List<String>> {
	List<String> valueList = null;

	@Override
	public boolean isValid(List<String> values, ConstraintValidatorContext context) {
		return values != null && valueList.containsAll(values);
	}
	
	@Override
    public void initialize(MultiValueEnumValidator constraintAnnotation) {
        valueList = new ArrayList<String>();
        Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClazz();

        @SuppressWarnings("rawtypes")
        Enum[] enumValArr = enumClass.getEnumConstants();
        for (@SuppressWarnings("rawtypes") Enum enumVal : enumValArr) {
            valueList.add(enumVal.toString());
        }
    }

}