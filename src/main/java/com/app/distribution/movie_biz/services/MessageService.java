package com.app.distribution.movie_biz.services;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.app.distribution.movie_biz.config.TwilioConfig;
import com.app.distribution.movie_biz.entites.DealDetailInfo;
import com.app.distribution.movie_biz.entites.EmailAuthRequest;
import com.app.distribution.movie_biz.entites.MobileAuthRequest;
import com.app.distribution.movie_biz.repositories.DealRepository;
import com.app.distribution.movie_biz.repositories.TheatreRepository;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.mail.MessagingException;

@Service
public class MessageService {
	private static final Integer EXPIRE_MIN = 5;
	@Autowired
	RedisTemplate<Object, Object> redisTemplate;
	@Autowired
	EmailService emailService;
	@Autowired
	TwilioConfig twilioConfig;
	@Autowired
	TheatreRepository theatreRepository;
	@Autowired
	DealRepository dealRepository;
	Logger logger = LoggerFactory.getLogger(MessageService.class);

	/**
	 * generates a random otp for a given email and stores it in redis cache
	 * 
	 * @param email
	 * @return otp
	 */
	private String getRandomEmailOtp(String email) {
		String otp = String.valueOf(new Random().nextInt(1000, 10000));
		redisTemplate.opsForValue().set(email, otp);
		redisTemplate.expire(email, EXPIRE_MIN, TimeUnit.MINUTES);
		return otp;
	}

	/**
	 * generates a random otp for a given mobile number and stores it in redis cache
	 * 
	 * @param mobileno
	 * @return
	 */
	private String getRandomOtp(String mobileno) {
		String otp = String.valueOf(new Random().nextInt(1000, 10000));
		redisTemplate.opsForValue().set(mobileno, otp);
		redisTemplate.expire(mobileno, EXPIRE_MIN, TimeUnit.MINUTES);
		return otp;
	}

	public MessageService() {
		super();
	}

	/**
	 * Generates an otp and sends the same to given email id
	 * 
	 * @param email
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 */
	public void generateOtpToEmail(String email) throws UnsupportedEncodingException, MessagingException {
		String otp = getRandomEmailOtp(email);
		String content = "Please find the OTP to login into App: " + otp;
		emailService.sendEmail(email, "OTP to login application", content);
	}

	/**
	 * validates the otp sent to an email and authorizes the user
	 * 
	 * @param emailAuthRequest
	 * @return
	 * @throws ExecutionException
	 */
	public boolean validateEmailOtp(EmailAuthRequest emailAuthRequest) throws ExecutionException {
		if (redisTemplate.opsForValue().get(emailAuthRequest.getEmail()) != null
				&& redisTemplate.opsForValue().get(emailAuthRequest.getEmail()).equals(emailAuthRequest.getOtp())) {
			logger.info("otp validation is successful");
			return true;
		}
		logger.error("invalid otp");
		return false;
	}

	/**
	 * otp is generated for a given mobile number and sends an SMS to the same
	 * mobile number
	 * 
	 * @param mobileNo
	 * @throws ApiException
	 */
	public void generateOtpToMobile(String mobileNo) throws ApiException {
		if (mobileNo != null) {
			PhoneNumber to = new PhoneNumber("+91" + mobileNo);
			String otpMessage = "Please find the OTP to login into App: " + getRandomOtp(mobileNo);
			Message.creator(to, twilioConfig.getServiceId(), otpMessage).create();
		}
	}

	/**
	 * validates the otp for a given mobile number and authorizes the user
	 * 
	 * @param mobileAuthRequest
	 * @return
	 * @throws Exception
	 */
	public boolean validateMobileOtp(MobileAuthRequest mobileAuthRequest) throws Exception {
		if (redisTemplate.opsForValue().get(mobileAuthRequest.getMobileNo()) != null && redisTemplate.opsForValue()
				.get(mobileAuthRequest.getMobileNo()).equals(mobileAuthRequest.getOtp())) {
			logger.info("otp validation is successful");
			return true;
		}
		logger.error("invalid otp");
		return false;
	}

	/**
	 * On purchase of a deal by investor, details of purchase are sent to the seller
	 * 
	 * @param dealid
	 */
	@Async
	public void sendTransMsgToSeller(int dealid) {
		DealDetailInfo dealDetailInfo = dealRepository.getDealDetailedInfo(dealid);
		long mobileno = theatreRepository.getUserbyTheatre(dealDetailInfo.getTheatreid());
		PhoneNumber to = new PhoneNumber("+91" + mobileno);
		String transMessage = "Deal purchased \n" + dealDetailInfo.getMoviename() + "\n" + dealDetailInfo.getShowdate()
				+ "\n" + dealDetailInfo.getShowtime() + " Show \n" + "Price Rs " + dealDetailInfo.getTotaldealprice();
		Message.creator(to, twilioConfig.getServiceId(), transMessage).create();
		logger.info("sent deal purchased details to seller");
	}
}