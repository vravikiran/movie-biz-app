package com.app.distribution.movie_biz.entites;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Range;

import com.app.distribution.movie_biz.util.IsValidPhoneNumber;

import jakarta.validation.constraints.Email;

public class UserDto {
	@Range(min = 1000000000L, max = 9999999999L)
	@IsValidPhoneNumber(message = "not a valid mobile number")
	private long mobileno;
	private String full_name;
	private String pan_number;
	@Email
	private String email;
	private LocalDate date_of_birth;

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

	public LocalDate getDate_of_birth() {
		return date_of_birth;
	}

	public void setDate_of_birth(LocalDate date_of_birth) {
		this.date_of_birth = date_of_birth;
	}
}