package com.app.distribution.movie_biz.services;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.distribution.movie_biz.entites.BankAccountDetails;
import com.app.distribution.movie_biz.entites.KycDetails;
import com.app.distribution.movie_biz.entites.Role;
import com.app.distribution.movie_biz.entites.UserDto;
import com.app.distribution.movie_biz.entites.UserInfoDetails;
import com.app.distribution.movie_biz.entites.UserProfile;
import com.app.distribution.movie_biz.enums.RoleTypeEnum;
import com.app.distribution.movie_biz.exceptions.DuplicateUserException;
import com.app.distribution.movie_biz.exceptions.InvalidBankDetailsException;
import com.app.distribution.movie_biz.exceptions.InvalidKycDetailsException;
import com.app.distribution.movie_biz.exceptions.UserNotFoundException;
import com.app.distribution.movie_biz.repositories.UserProfileRepository;
import com.app.distribution.movie_biz.util.Constants;
import com.app.distribution.movie_biz.util.HashGenerator;
import com.app.distribution.movie_biz.util.KmsUtil;

@Service
public class UserProfileService implements UserDetailsService {
	@Autowired
	UserProfileRepository userProfileRepository;
	@Autowired
	KmsUtil kmsUtil;

	Logger logger = LoggerFactory.getLogger(UserProfileService.class);

	/**
	 * creates a user profile with given information.
	 * handles duplicates user
	 * based on isAdminUser creates an admin user profile or an investor user profile
	 * @param userDto
	 * @param isAdminUser
	 * @return
	 * @throws Exception
	 */
	public UserProfile createUserProfile(UserDto userDto, boolean isAdminUser) throws Exception {
		logger.info("Creation of user profile started :: ");
		UserProfile userProfile = new UserProfile();
		if (Long.valueOf(userDto.getMobileno()) != null) {
			if (isUserExistsByMobileNumber(userDto.getMobileno())) {
				logger.info("Creation of user profile failed as user already exists with given mobile number");
				throw new DuplicateUserException("user already exists with given mobile number");
			} else {
				userProfile.setMobileno_hash(HashGenerator.generateSHA256Hash(Long.toString(userDto.getMobileno())));
			}
		}
		if (userDto.getPan_number() != null) {
			if (isUserExistsWithPanNo(userDto.getPan_number())) {
				logger.info("Creation of user profile failed as user already exists with given pan number");
				throw new DuplicateUserException("user already exists with given pan number");
			} else {
				userProfile.setPanno_hash(HashGenerator.generateSHA256Hash(userDto.getPan_number()));
			}
		}
		if (userDto.getEmail() != null) {
			if (isUserExistsWithEmail(userDto.getEmail())) {
				logger.info("Creation of user profile failed as user already exists with given email");
				throw new DuplicateUserException("user already exists with given email");
			} else {
				userProfile.setEmail_hash(HashGenerator.generateSHA256Hash(userDto.getEmail()));
			}
		}
		userProfile.setCreated_date(ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate());
		userProfile.setUpdated_date(ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate());
		Role role = null;
		if (isAdminUser) {
			role = new Role(RoleTypeEnum.ADMIN.getRoleid(), RoleTypeEnum.ADMIN.getRole_name());
		} else {
			role = new Role(RoleTypeEnum.INVESTOR.getRoleid(), RoleTypeEnum.INVESTOR.getRole_name());
		}
		userProfile.setMobileno(userDto.getMobileno());
		userProfile.setEmail(userDto.getEmail());
		userProfile.setFull_name(userDto.getFull_name());
		userProfile.setPan_number(userDto.getPan_number());
		userProfile.setDate_of_birth(userDto.getDate_of_birth());
		userProfile.setRole(role);
		UserProfile createdUserProfile = userProfileRepository.save(userProfile);
		logger.info("user profile created successfully for given mobile number :: " + userProfile.getMobileno());
		return createdUserProfile;
	}

	/**
	 * retrieves user profile based on given mobile number
	 * @param mobileno
	 * @return
	 * @throws UserNotFoundException
	 */
	public UserProfile getUserProfile(long mobileno) throws UserNotFoundException {
		logger.info("Fetching user profile for given mobile number :: " + mobileno);
		if (isUserExistsByMobileNumber(mobileno)) {
			logger.info("successfully fetched user profile with given mobile number :: " + mobileno);
			return userProfileRepository.getReferenceById(generateMobileNumberHash(mobileno));
		} else {
			logger.info("User profile not found with given mobile number :: " + mobileno);
			throw new UserNotFoundException("User not found with given mobile number");
		}
	}

	/**
	 * updates the bank account details only for investors
	 * @param mobileno
	 * @param bankAccountDetails
	 * @throws UserNotFoundException
	 * @throws InvalidBankDetailsException
	 */
	public void addBankAccDetailsToProfile(long mobileno, BankAccountDetails bankAccountDetails)
			throws UserNotFoundException, InvalidBankDetailsException {
		logger.info("verifying bank account details and adding it to user profile with mobile number :: " + mobileno
				+ " started");
		UserProfile userProfile = getUserProfile(mobileno);
		bankAccountDetails.setCreated_date(ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate());
		bankAccountDetails.setUpdated_date(ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate());
		userProfile.setBankAccountDetails(bankAccountDetails);
		userProfile.setBank_details_verified(true);
		userProfile.setUpdated_date(ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate());
		userProfileRepository.save(userProfile);
		logger.info("bank account details are verified successfully and added it to user profile with mobile number :: "
				+ mobileno);
	}

	/**
	 * Updates the kyc details of a specific user
	 * @param mobileno
	 * @param kycDetails
	 * @throws UserNotFoundException
	 * @throws InvalidKycDetailsException
	 */
	public void verifyAndAddKycDetails(long mobileno, KycDetails kycDetails)
			throws UserNotFoundException, InvalidKycDetailsException {
		logger.info("verifying kyc details and adding it to user profile with given mobile number :: " + mobileno
				+ " started");
		UserProfile userProfile = getUserProfile(mobileno);
		userProfile.setKycDetails(kycDetails);
		userProfile.setAadhar_verified(true);
		userProfile.setUpdated_date(ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate());
		userProfileRepository.save(userProfile);
		logger.info(
				"kyc details are verified successfully and added it to user profile with mobile number :: " + mobileno);
	}

	/**
	 * updates user details based on given information
	 * @param mobileno
	 * @param valuesToUpdate
	 * @return
	 * @throws UserNotFoundException
	 * @throws DuplicateUserException
	 * @throws NoSuchElementException
	 */
	public UserProfile updateUserDetails(long mobileno, Map<String, Object> valuesToUpdate)
			throws UserNotFoundException, DuplicateUserException, NoSuchElementException {
		UserProfile userProfile = getUserProfile(mobileno);
		UserProfile updatedUserProfile = null;
		if (valuesToUpdate.containsKey(Constants.PAN_NO)) {
			if (valuesToUpdate.get(Constants.PAN_NO) != null) {
				if (!isUserExistsWithPanNo(valuesToUpdate.get(Constants.PAN_NO).toString())) {
					valuesToUpdate.put(Constants.PAN_NO, valuesToUpdate.get(Constants.PAN_NO));
					valuesToUpdate.put(Constants.PANNO_HASH,
							HashGenerator.generateSHA256Hash(valuesToUpdate.get(Constants.PANNO_HASH).toString()));
				} else {
					logger.info("user already exists with given pan number");
					throw new DuplicateUserException("user already exists with given pan number");
				}
			}
		}
		if (valuesToUpdate.containsKey(Constants.EMAIL)) {
			if (valuesToUpdate.get(Constants.EMAIL) != null) {
				if (!isUserExistsWithEmail(valuesToUpdate.get(Constants.EMAIL).toString())) {
					valuesToUpdate.put(Constants.EMAIL, valuesToUpdate.get(Constants.EMAIL).toString());
					valuesToUpdate.put(Constants.EMAIL_HASH,
							HashGenerator.generateSHA256Hash(valuesToUpdate.get(Constants.EMAIL).toString()));
				} else {
					logger.info("user already exists with given email");
					throw new DuplicateUserException("user already exists with given email");
				}
			}
		}
		try {
			userProfile.updateValues(userProfile, valuesToUpdate);
			userProfile.setUpdated_date(ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate());
		} catch (NoSuchElementException exception) {
			throw new NoSuchElementException("one or more fields are not valid");
		}
		updatedUserProfile = userProfileRepository.save(userProfile);
		return updatedUserProfile;
	}

	/**
	 * loads user information with given mobile number
	 * 
	 * @param username
	 * @throws UsernameNotFoundException
	 */
	public UserDetails loadUserByUsername(String username) {
		Optional<UserProfile> userProfile = userProfileRepository.findById(HashGenerator.generateSHA256Hash(username));
		return userProfile.map(UserInfoDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with given mobileNo " + username));
	}

	/**
	 * loads user information based on the given email id
	 * 
	 * @param email
	 * @return
	 * @throws UsernameNotFoundException, if user not found with given email
	 */
	public UserInfoDetails loadUserByEmail(String email) throws UsernameNotFoundException {
		Optional<UserProfile> userProfile = userProfileRepository.findByEmail(HashGenerator.generateSHA256Hash(email));
		return userProfile.map(UserInfoDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with given email " + email));
	}

	/**
	 * verifies if a user exists with a given mobile number
	 * 
	 * @param mobileNo
	 * @return
	 */
	private boolean isUserExistsByMobileNumber(long mobileNo) {
		return userProfileRepository.existsById(generateMobileNumberHash(mobileNo));
	}

	/**
	 * verifies if a user exists with given email
	 * 
	 * @param email
	 * @return
	 */
	private boolean isUserExistsWithEmail(String email) {
		return userProfileRepository.existsByEmail(HashGenerator.generateSHA256Hash(email));
	}

	/**
	 * verifies if a user exists with given PAN number
	 * 
	 * @param panNumber
	 * @return
	 */
	private boolean isUserExistsWithPanNo(String panNumber) {
		return userProfileRepository.existsByPanNumber(HashGenerator.generateSHA256Hash(panNumber));
	}

	/**
	 * generates a hash value for a given mobile number
	 * 
	 * @param mobileNo
	 * @return
	 */
	private String generateMobileNumberHash(long mobileNo) {
		return HashGenerator.generateSHA256Hash(Long.toString(mobileNo));
	}
}