package com.app.distribution.movie_biz.entites;

import java.time.LocalDate;
import java.util.Objects;

import com.app.distribution.movie_biz.util.EncryptDecryptHelper;
import com.app.distribution.movie_biz.util.IsValidAccountNumber;
import com.app.distribution.movie_biz.util.LongStringConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Table(name = "user_bank_account_details")
@Entity
public class BankAccountDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int bank_acc_details_id;
	@Nonnull
	@Convert(converter = EncryptDecryptHelper.class)
	private String account_holder_name;
	@Nonnull
	@IsValidAccountNumber(message = "account number must be between 11 and 16 digits")
	@Convert(converter = LongStringConverter.class)
	private long account_number;
	@Nonnull
	@Convert(converter = EncryptDecryptHelper.class)
	private String ifsc_code;
	@Nonnull
	private LocalDate created_date;
	@Nonnull
	private LocalDate updated_date;
	@OneToOne(mappedBy = "bankAccountDetails")
	@JsonIgnore
	private UserProfile userProfile;

	public int getBank_acc_details_id() {
		return bank_acc_details_id;
	}

	public void setBank_acc_details_id(int bank_acc_details_id) {
		this.bank_acc_details_id = bank_acc_details_id;
	}

	public String getAccount_holder_name() {
		return account_holder_name;
	}

	public void setAccount_holder_name(String account_holder_name) {
		this.account_holder_name = account_holder_name;
	}

	public long getAccount_number() {
		return account_number;
	}

	public void setAccount_number(long account_number) {
		this.account_number = account_number;
	}

	public String getIfsc_code() {
		return ifsc_code;
	}

	public void setIfsc_code(String ifsc_code) {
		this.ifsc_code = ifsc_code;
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

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	@Override
	public int hashCode() {
		return Objects.hash(bank_acc_details_id, account_holder_name, account_number, created_date, ifsc_code,
				updated_date, userProfile);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BankAccountDetails other = (BankAccountDetails) obj;
		return bank_acc_details_id == other.bank_acc_details_id
				&& Objects.equals(account_holder_name, other.account_holder_name)
				&& account_number == other.account_number && Objects.equals(created_date, other.created_date)
				&& Objects.equals(ifsc_code, other.ifsc_code) && Objects.equals(updated_date, other.updated_date)
				&& Objects.equals(userProfile, other.userProfile);
	}

	public BankAccountDetails() {
		super();
	}

}