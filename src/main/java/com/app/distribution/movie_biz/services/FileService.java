package com.app.distribution.movie_biz.services;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.distribution.movie_biz.entites.UserProfile;
import com.app.distribution.movie_biz.exceptions.UserNotFoundException;
import com.app.distribution.movie_biz.repositories.UserProfileRepository;
import com.app.distribution.movie_biz.util.HashGenerator;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class FileService {
	@Autowired
	UserProfileRepository userProfileRepository;
	Logger logger = LoggerFactory.getLogger(FileService.class);
	@Autowired
	S3Client s3Client;

	/**
	 * Uploads user profile image in an S3 bucket. Throws an exception if a user is
	 * not found with given mobile number Throws an exception if app is unable to
	 * connect to the S3
	 * 
	 * @param file
	 * @param mobileno
	 * @param bucketName
	 * @return
	 * @throws IOException
	 * @throws UserNotFoundException
	 */
	public String uploadProfileImage(MultipartFile file, long mobileno, String bucketName)
			throws IOException, UserNotFoundException {
		logger.info("Upload image for user profile with mobile number ::" + mobileno);
		String key = Long.valueOf(mobileno) + "/" + LocalDateTime.now() + "_" + file.getOriginalFilename();
		URL url = imageUploadFromMobile(file, bucketName, key);
		logger.info("url of the uploaded image :: " + url.toString());
		UserProfile userProfile = userProfileRepository
				.getReferenceById(HashGenerator.generateSHA256Hash(Long.toString(mobileno)));
		userProfile.setUser_image_url(url.toString());
		userProfile.setUpdated_date(ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate());
		userProfileRepository.save(userProfile);
		logger.info("profile image uploaded successfully");
		return url.toString();
	}

	/**
	 * Uploads kyc images of a specific user in an S3 bucket Throws an exception if
	 * a user is not found with given mobile number Throws an exception if app is
	 * unable to connect to the S3
	 * 
	 * @param file
	 * @param mobileno
	 * @param bucketName
	 * @return
	 * @throws S3Exception
	 * @throws AwsServiceException
	 * @throws SdkClientException
	 * @throws IOException
	 */
	public String uploadKycImage(MultipartFile file, long mobileno, String bucketName)
			throws S3Exception, AwsServiceException, SdkClientException, IOException {
		logger.info("Upload kyc image for user profile with mobile number ::" + mobileno);
		String key = Long.valueOf(mobileno) + "/" + LocalDateTime.now() + "_" + file.getOriginalFilename();
		URL url = imageUploadFromMobile(file, bucketName, key);
		logger.info("kyc image uploaded successfully");
		return url.toString();
	}

	/**
	 * uploads a movie image in S3 bucket Throws an exception if a movie with given
	 * id doesn't exists Throws an exception if app is unable to connect to the S3
	 * 
	 * @param file
	 * @param bucketName
	 * @return
	 * @throws S3Exception
	 * @throws AwsServiceException
	 * @throws SdkClientException
	 * @throws IOException
	 */
	public String uploadMovieImage(MultipartFile file, String bucketName)
			throws S3Exception, AwsServiceException, SdkClientException, IOException {
		logger.info("Upload movie image started");
		URL url = imageUploadFromMobile(file, bucketName, file.getOriginalFilename());
		logger.info("movie image uploaded successfully");
		return url.toString();
	}

	/**
	 * common method to upload image into an S3 bucket
	 * 
	 * @param file
	 * @param bucketName
	 * @param key
	 * @return
	 * @throws IOException
	 */
	private URL imageUploadFromMobile(MultipartFile file, String bucketName, String key) throws IOException {
		PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(key).build();
		s3Client.putObject(putObjectRequest,
				RequestBody.fromInputStream(file.getInputStream(), file.getBytes().length));
		GetUrlRequest request = GetUrlRequest.builder().bucket(bucketName).key(key).build();
		URL url = s3Client.utilities().getUrl(request);
		return url;
	}
}