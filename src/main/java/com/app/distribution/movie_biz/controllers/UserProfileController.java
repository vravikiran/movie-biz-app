package com.app.distribution.movie_biz.controllers;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.app.distribution.movie_biz.entites.BankAccountDetails;
import com.app.distribution.movie_biz.entites.KycDetails;
import com.app.distribution.movie_biz.entites.UserDto;
import com.app.distribution.movie_biz.entites.UserProfile;
import com.app.distribution.movie_biz.exceptions.DuplicateUserException;
import com.app.distribution.movie_biz.exceptions.InvalidBankDetailsException;
import com.app.distribution.movie_biz.exceptions.InvalidKycDetailsException;
import com.app.distribution.movie_biz.exceptions.UserNotFoundException;
import com.app.distribution.movie_biz.services.FileService;
import com.app.distribution.movie_biz.services.UserProfileService;
import com.app.distribution.movie_biz.util.Constants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(description = "Creates/updates user profiles, store kyc details and bank account details of users", name = "User Profile Management")
@Controller
@RequestMapping("/userprofile")
public class UserProfileController {
	Logger logger = LoggerFactory.getLogger(UserProfileController.class);
	@Autowired
	UserProfileService userProfileService;
	@Autowired
	FileService fileService;

	@Operation(method = "POST", description = "Creates a customer user profile")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "User profile created succesfully"),
			@ApiResponse(responseCode = "400", description = "User profile already exists with given details") })
	@PostMapping
	public ResponseEntity<UserProfile> createUserProfile(@RequestBody UserDto userDto)
			throws DuplicateUserException, Exception {
		logger.info("creating user profile started :: ");
		UserProfile createdUserProfile = userProfileService.createUserProfile(userDto, false);
		logger.info("creation of user profile successful");
		return ResponseEntity.ok(createdUserProfile);
	}

	@Operation(method = "POST", description = "Creates an admin user profile")
	@ApiResponses(@ApiResponse(responseCode = "200", description = "Admin user created successfully"))
	@Parameters({ @Parameter(in = ParameterIn.HEADER, name = "Authorization", required = true),
			@Parameter(in = ParameterIn.HEADER, name = "TOKEN_TYPE", required = true) })
	@PostMapping("/admin")
	@PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
	public ResponseEntity<UserProfile> createAdminUser(@RequestBody UserDto userDto)
			throws DuplicateUserException, Exception {
		logger.info("creating Admin user profile started :: ");
		UserProfile createdUserProfile = userProfileService.createUserProfile(userDto, true);
		logger.info("creation of Admin user profile successful");
		return ResponseEntity.ok(createdUserProfile);
	}

	@Operation(method = "GET", description = "Fetches user profile based on mobile number")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "User profile exists and returns user profile"),
			@ApiResponse(responseCode = "404", description = "User profile with given mobile number doesn't exists") })
	@GetMapping
	public ResponseEntity<UserProfile> getUserProfile(@RequestParam long mobileno) throws UserNotFoundException {
		logger.info("fetching user profile with mobile number :: " + mobileno);
		UserProfile userProfile = userProfileService.getUserProfile(mobileno);
		logger.info("user profile fetched successfully");
		return ResponseEntity.ok(userProfile);
	}

	@Operation(method = "PATCH", description = "Updates user profile")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Updates user profile, provided valid columns of a user profile"),
			@ApiResponse(responseCode = "422", description = "throws an exception if a user profile already exists with provided mobile number, email or pan number. Provides appropriate error message"),
			@ApiResponse(responseCode = "404", description = "User profile with given mobile number doesn't exists") })
	@PatchMapping("/update")
	public ResponseEntity<String> updateUserProfile(@RequestBody Map<String, Object> valuesToUpdate,
			@RequestParam long mobileNo) throws UserNotFoundException, DuplicateUserException {
		logger.info("Updating user profile with given mobile number :: " + mobileNo);
		userProfileService.updateUserDetails(mobileNo, valuesToUpdate);
		logger.info("user profile updated successfully with given mobile number:: " + mobileNo);
		return ResponseEntity.ok("user profile updated successfully");
	}

	@Operation(method = "POST", description = "Adds the bank details to user profile")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Adds the bank account details to user profile"),
			@ApiResponse(responseCode = "404", description = "User profile with given mobile number doesn't exists"),
			@ApiResponse(responseCode = "400", description = "The provided bank account details are incorrect") })
	@PostMapping("/bankdetails")
	@PreAuthorize("hasAnyAuthority('INVESTOR')")
	public ResponseEntity<String> addAccountDetailsToProfile(@RequestParam long mobileno,
			@RequestBody BankAccountDetails bankAccountDetails)
			throws UserNotFoundException, InvalidBankDetailsException {
		logger.info("Addition of bank account details to user profile with mobile number :: " + mobileno + " started");
		userProfileService.addBankAccDetailsToProfile(mobileno, bankAccountDetails);
		logger.info("Addition of bank details completed successfully to user profile with mobile number ::" + mobileno);
		return ResponseEntity.ok("account details added successfully to user profile");
	}

	@Operation(method = "POST", description = "Adds kyc details to user profile")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Adds kyc details to user profile"),
			@ApiResponse(responseCode = "404", description = "User profile with given mobile number doesn't exists"),
			@ApiResponse(responseCode = "400", description = "The provided kyc details are incorrect") })
	@PostMapping("/kyc")
	public ResponseEntity<String> addKycDetailsToProfile(@RequestParam long mobileno,
			@RequestBody KycDetails kycDetails) throws UserNotFoundException, InvalidKycDetailsException {
		logger.info("adding kyc details to user profile with mobile number :: " + mobileno + " started");
		userProfileService.verifyAndAddKycDetails(mobileno, kycDetails);
		logger.info("kyc details successfully added to user profile with mobile number :: " + mobileno);
		return ResponseEntity.ok("kyc details added successfully to user profile");
	}

	@Operation(method = "POST", description = "Uploads profile image/picutres of user and updates the same in user profile")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Image uploaded successfully and updated the same in user profile"),
			@ApiResponse(responseCode = "404", description = "User profile with given mobile number doesn't exists") })
	@PostMapping(path = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> uploadProfileImage(@RequestPart("photo") MultipartFile file,
			@RequestParam long mobileno) throws IOException, UserNotFoundException {
		logger.info("uploading profile image for user profile with mobile number :: " + mobileno);
		String url = fileService.uploadProfileImage(file, mobileno, Constants.PROFILE_BUCKET);
		logger.info("profile image uploaded successfully for user profile");
		return ResponseEntity.ok(url);
	}

	@Operation(method = "POST", description = "Uploads images related to kyc of user and returns the s3 URI")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Kyc image uploaded successfully"),
			@ApiResponse(responseCode = "404", description = "User profile with given mobile number doesn't exists") })
	@PostMapping(path = "/kycimage/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> uploadKycImage(@RequestPart("photo") MultipartFile file, @RequestParam long mobileno)
			throws IOException, UserNotFoundException {
		logger.info("uploading kyc image for user profile with mobile number :: " + mobileno);
		String url = fileService.uploadKycImage(file, mobileno, Constants.KYC_BUCKET);
		logger.info("kyc image uploaded successfully for user profile");
		return ResponseEntity.ok(url);
	}

}