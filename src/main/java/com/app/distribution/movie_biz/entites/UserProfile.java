package com.app.distribution.movie_biz.entites;

import java.time.LocalDate;
import java.util.Objects;

import org.hibernate.validator.constraints.Range;

import com.app.distribution.movie_biz.util.DateStringConverter;
import com.app.distribution.movie_biz.util.EncryptDecryptHelper;
import com.app.distribution.movie_biz.util.IsValidPhoneNumber;
import com.app.distribution.movie_biz.util.LongStringConverter;
import com.app.distribution.movie_biz.util.PatchableObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Table(name = "user_profile")
@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer" })
public class UserProfile extends PatchableObject {
	@Range(min = 1000000000L, max = 9999999999L)
	@IsValidPhoneNumber(message = "not a valid mobile number")
	@Convert(converter = LongStringConverter.class)
	private long mobileno;
	@JsonIgnore
	@Id
	private String mobileno_hash;
	@Convert(converter = EncryptDecryptHelper.class)
	private String full_name;
	@Convert(converter = EncryptDecryptHelper.class)
	private String pan_number;
	@Convert(converter = EncryptDecryptHelper.class)
	private String email;
	private String user_image_url;
	private LocalDate created_date;
	private LocalDate updated_date;
	@Convert(converter = DateStringConverter.class)
	private LocalDate date_of_birth;
	private boolean aadhar_verified;
	private boolean bank_details_verified;
	@JsonIgnore
	private String email_hash;
	@JsonIgnore
	private String panno_hash;

	public String getEmail_hash() {
		return email_hash;
	}

	public void setEmail_hash(String email_hash) {
		this.email_hash = email_hash;
	}

	public String getPanno_hash() {
		return panno_hash;
	}

	public void setPanno_hash(String panno_hash) {
		this.panno_hash = panno_hash;
	}

	public boolean isBank_details_verified() {
		return bank_details_verified;
	}

	public void setBank_details_verified(boolean bank_details_verified) {
		this.bank_details_verified = bank_details_verified;
	}

	public boolean isAadhar_verified() {
		return aadhar_verified;
	}

	public void setAadhar_verified(boolean aadhar_verified) {
		this.aadhar_verified = aadhar_verified;
	}

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "role_id", referencedColumnName = "role_id")
	private Role role;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "kyc_id", referencedColumnName = "kyc_id")
	private KycDetails kycDetails;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "bank_acc_details_id", referencedColumnName = "bank_acc_details_id")
	private BankAccountDetails bankAccountDetails;

	public KycDetails getKycDetails() {
		return kycDetails;
	}

	public void setKycDetails(KycDetails kycDetails) {
		this.kycDetails = kycDetails;
	}

	public BankAccountDetails getBankAccountDetails() {
		return bankAccountDetails;
	}

	public void setBankAccountDetails(BankAccountDetails bankAccountDetails) {
		this.bankAccountDetails = bankAccountDetails;
	}

	public long getMobileno() {
		return mobileno;
	}

	public void setMobileno(long mobileno) {
		this.mobileno = mobileno;
	}

	public String getFull_name() {
		return full_name;
	}

	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}

	public String getPan_number() {
		return pan_number;
	}

	public void setPan_number(String pan_number) {
		this.pan_number = pan_number;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUser_image_url() {
		return user_image_url;
	}

	public void setUser_image_url(String user_image_url) {
		this.user_image_url = user_image_url;
	}

	public LocalDate getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDate created_date) {
		this.created_date = created_date;
	}

	public LocalDate getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(LocalDate updated_date) {
		this.updated_date = updated_date;
	}

	public LocalDate getDate_of_birth() {
		return date_of_birth;
	}

	public void setDate_of_birth(LocalDate date_of_birth) {
		this.date_of_birth = date_of_birth;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getMobileno_hash() {
		return mobileno_hash;
	}

	public void setMobileno_hash(String mobileno_hash) {
		this.mobileno_hash = mobileno_hash;
	}

	@Override
	public int hashCode() {
		return Objects.hash(aadhar_verified, bankAccountDetails, bank_details_verified, created_date, date_of_birth,
				email, email_hash, full_name, kycDetails, mobileno, mobileno_hash, pan_number, panno_hash, role,
				updated_date, user_image_url);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserProfile other = (UserProfile) obj;
		return aadhar_verified == other.aadhar_verified && Objects.equals(bankAccountDetails, other.bankAccountDetails)
				&& bank_details_verified == other.bank_details_verified
				&& Objects.equals(created_date, other.created_date)
				&& Objects.equals(date_of_birth, other.date_of_birth) && Objects.equals(email, other.email)
				&& Objects.equals(email_hash, other.email_hash) && Objects.equals(full_name, other.full_name)
				&& Objects.equals(kycDetails, other.kycDetails) && mobileno == other.mobileno
				&& Objects.equals(mobileno_hash, other.mobileno_hash) && Objects.equals(pan_number, other.pan_number)
				&& Objects.equals(panno_hash, other.panno_hash) && Objects.equals(role, other.role)
				&& Objects.equals(updated_date, other.updated_date)
				&& Objects.equals(user_image_url, other.user_image_url);
	}

	@Override
	public String toString() {
		return "UserProfile [mobileno=" + mobileno + ", mobileno_hash=" + mobileno_hash + ", full_name=" + full_name
				+ ", pan_number=" + pan_number + ", email=" + email + ", user_image_url=" + user_image_url
				+ ", created_date=" + created_date + ", updated_date=" + updated_date + ", date_of_birth="
				+ date_of_birth + ", aadhar_verified=" + aadhar_verified + ", bank_details_verified="
				+ bank_details_verified + ", email_hash=" + email_hash + ", panno_hash=" + panno_hash + ", role=" + role
				+ ", kycDetails=" + kycDetails + ", bankAccountDetails=" + bankAccountDetails + "]";
	}
	
}