package com.app.distribution.movie_biz.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.app.distribution.movie_biz.entites.UserInfoDetails;
import com.app.distribution.movie_biz.services.UserProfileService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtHelper {
	@Autowired
	UserProfileService userProfileService;
	@Autowired
	SecretKeyGenerator keyGenerator;
	Logger logger = LoggerFactory.getLogger(JwtHelper.class);

	public String generateTokenByMobileNo(String mobileNo) {
		Instant now = Instant.now();
		return Jwts.builder().subject(mobileNo).issuedAt(Date.from(now))
				.expiration(Date.from(now.plus(60, ChronoUnit.MINUTES))).signWith(keyGenerator.getSecretKey()).compact();
	}

	public String generateTokenByEmail(String email) {
		Instant now = Instant.now();
		return Jwts.builder().subject(email).issuedAt(Date.from(now))
				.expiration(Date.from(now.plus(5, ChronoUnit.MINUTES))).signWith(keyGenerator.getSecretKey()).compact();
	}

	public boolean vaildateToken(String token) {
		String mobileNo = null;
		try {
		 mobileNo = extractMobileNo(token);
		} catch(SignatureException exception) {
			logger.info(exception.getMessage());
		}
		if(mobileNo != null) {
		UserDetails userDetails = userProfileService.loadUserByUsername(mobileNo);
		return userDetails.getUsername().equals(mobileNo) && isTokenExpired(token);
		}  else 
			return false;
	}
	
	public boolean vaildateEmailToken(String token) {
		String email = null;
		try {
		 email = extractEmail(token);
		} catch(SignatureException exception) {
			logger.info(exception.getMessage());
		}
		if(email != null) {
		UserInfoDetails userDetails = userProfileService.loadUserByEmail(email);
		return userDetails.getEmail().equals(email) && isTokenExpired(token);
		}  else 
			return false;
	}

	public Claims getTokenBody(String token) {
		Claims claims = Jwts.parser().verifyWith(keyGenerator.getSecretKey()).build().parseSignedClaims(token)
				.getPayload();
		return claims;
	}

	public String extractMobileNo(String token) {
		return getTokenBody(token).getSubject();
	}
	
	public String extractEmail(String token) {
		return getTokenBody(token).getSubject();
	}

	private Boolean isTokenExpired(String token) {
		return Date.from(Instant.now()).before(getTokenBody(token).getExpiration());
	}
}