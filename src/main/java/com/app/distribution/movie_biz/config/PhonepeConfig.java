package com.app.distribution.movie_biz.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * PhonePe app configuration which is used to receive the payment response once
 * user completes the payment
 */
@Configuration
public class PhonepeConfig {
	@Value("${phonepe.merchant.id}")
	private String phonePeMerchantId;
	@Value("${phonepe.salt.index}")
	private String phonePeSaltIndex;
	@Value("${phonepe.salt.key}")
	private String phonePeSaltKey;

	public String getPhonePeSaltIndex() {
		return phonePeSaltIndex;
	}

	public String getPhonePeSaltKey() {
		return phonePeSaltKey;
	}

	public String getPhonePeMerchantId() {
		return phonePeMerchantId;
	}
}