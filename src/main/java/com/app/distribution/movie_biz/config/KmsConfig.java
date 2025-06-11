package com.app.distribution.movie_biz.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuring A KMS key to encrypt and decrypt PII data
 */
@Configuration
@ConfigurationProperties(prefix = "aws")
public class KmsConfig {
	@Value("${aws.kms.key}")
	private String key;

	public KmsConfig() {
		super();
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public KmsConfig(String key) {
		super();
		this.key = key;
	}
}