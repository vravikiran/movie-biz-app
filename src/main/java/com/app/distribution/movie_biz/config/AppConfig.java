package com.app.distribution.movie_biz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.twilio.Twilio;

import jakarta.annotation.PostConstruct;

/**
 * Initiated the twillio api to send sms messages to the user
 */
@Configuration
public class AppConfig {
	@Autowired
	TwilioConfig twilioConfig;

	@PostConstruct
	public void init() {
		Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
	}

}