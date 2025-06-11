package com.app.distribution.movie_biz.util;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EncryptDecryptHelper implements AttributeConverter<String, String> {

	@Autowired
	KmsUtil kmsUtil;

	@Override
	public String convertToDatabaseColumn(String attribute) {
		return attribute != null ? kmsUtil.kmsEncrypt(attribute) : null;
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		return dbData != null ? kmsUtil.kmsDecrypt(dbData) : null;
	}

}