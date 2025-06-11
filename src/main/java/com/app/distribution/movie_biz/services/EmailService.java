package com.app.distribution.movie_biz.services;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	@Value("${from.email.address}")
	private String fromEmailAddress;

	@Autowired
	private JavaMailSender mailSender;

	/**
	 * sends an otp message to the email for logging to the app based on the provided details
	 * @param recipient
	 * @param subject
	 * @param content
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 */
	@Async
	public void sendEmail(String recipient, String subject, String content)
			throws UnsupportedEncodingException, MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom(fromEmailAddress);
		helper.setTo(recipient);
		helper.setSubject(subject);
		helper.setText(content, true);
		mailSender.send(message);
	}
}