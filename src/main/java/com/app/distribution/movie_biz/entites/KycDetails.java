package com.app.distribution.movie_biz.entites;

import java.time.LocalDate;
import java.util.Objects;

import com.app.distribution.movie_biz.util.DateStringConverter;
import com.app.distribution.movie_biz.util.EncryptDecryptHelper;
import com.app.distribution.movie_biz.util.LongStringConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Table(name = "user_kyc_details")
@Entity
public class KycDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int kyc_id;
	@Convert(converter = EncryptDecryptHelper.class)
	private String first_name;
	@Convert(converter = EncryptDecryptHelper.class)
	private String last_name;
	@Convert(converter = DateStringConverter.class)
	private LocalDate date_of_birth;
	@Convert(converter = LongStringConverter.class)
	private long aadhar_card_number;
	private long mobile_no;
	private String aadhar_image_front_url;
	private String aadhar_image_back_url;
	@OneToOne(mappedBy = "kycDetails")
	@JsonIgnore
	private UserProfile userProfile;

	public int getKyc_id() {
		return kyc_id;
	}

	public void setKyc_id(int kyc_id) {
		this.kyc_id = kyc_id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public LocalDate getDate_of_birth() {
		return date_of_birth;
	}

	public void setDate_of_birth(LocalDate date_of_birth) {
		this.date_of_birth = date_of_birth;
	}

	public long getAadhar_card_number() {
		return aadhar_card_number;
	}

	public void setAadhar_card_number(long aadhar_card_number) {
		this.aadhar_card_number = aadhar_card_number;
	}

	public long getMobile_no() {
		return mobile_no;
	}

	public void setMobile_no(long mobile_no) {
		this.mobile_no = mobile_no;
	}

	public String getAadhar_image_front_url() {
		return aadhar_image_front_url;
	}

	public void setAadhar_image_front_url(String aadhar_image_front_url) {
		this.aadhar_image_front_url = aadhar_image_front_url;
	}

	public String getAadhar_image_back_url() {
		return aadhar_image_back_url;
	}

	public void setAadhar_image_back_url(String aadhar_image_back_url) {
		this.aadhar_image_back_url = aadhar_image_back_url;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	@Override
	public int hashCode() {
		return Objects.hash(aadhar_card_number, aadhar_image_back_url, aadhar_image_front_url, date_of_birth,
				first_name, kyc_id, last_name, mobile_no, userProfile);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KycDetails other = (KycDetails) obj;
		return aadhar_card_number == other.aadhar_card_number
				&& Objects.equals(aadhar_image_back_url, other.aadhar_image_back_url)
				&& Objects.equals(aadhar_image_front_url, other.aadhar_image_front_url)
				&& Objects.equals(date_of_birth, other.date_of_birth) && Objects.equals(first_name, other.first_name)
				&& kyc_id == other.kyc_id && Objects.equals(last_name, other.last_name) && mobile_no == other.mobile_no
				&& Objects.equals(userProfile, other.userProfile);
	}

	public KycDetails() {
		super();
	}

}