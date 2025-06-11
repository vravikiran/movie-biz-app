package com.app.distribution.movie_biz.util;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class LongStringConverter implements AttributeConverter<Long, String> {
	@Autowired
	KmsUtil kmsUtil;

	@Override
	public String convertToDatabaseColumn(Long attribute) {
	String value =	Long.toString(attribute.longValue());
		return kmsUtil.kmsEncrypt(value);
	}

	@Override
	public Long convertToEntityAttribute(String dbData) {
		return Long.valueOf(kmsUtil.kmsDecrypt(dbData));
	}

}