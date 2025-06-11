package com.app.distribution.movie_biz.util;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.AttributeConverter;

public class DateStringConverter implements AttributeConverter<LocalDate, String> {
	@Autowired
	KmsUtil kmsUtil;

	@Override
	public String convertToDatabaseColumn(LocalDate attribute) {
		// TODO Auto-generated method stub
		return attribute != null ? kmsUtil.kmsEncrypt(attribute.toString()) : null;
	}

	@Override
	public LocalDate convertToEntityAttribute(String dbData) {
		// TODO Auto-generated method stub
		return dbData != null ? LocalDate.parse(kmsUtil.kmsDecrypt(dbData)) : null;
	}

}