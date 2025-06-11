package com.app.distribution.movie_biz.util;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class SecretKeyGenerator {
	private SecretKey secretKey;

	public SecretKeyGenerator() {
		this.secretKey = Jwts.SIG.HS256.key().build();
	}

	public SecretKey getSecretKey() {
		return secretKey;
	}
}