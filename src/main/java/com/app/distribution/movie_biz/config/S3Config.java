package com.app.distribution.movie_biz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.services.s3.S3Client;

/**
 * defines configuration to connect with AWS S3 to store images
 */
@Configuration
public class S3Config {
	@Bean
	S3Client s3Client() {
		return S3Client.builder().build();
	}
}