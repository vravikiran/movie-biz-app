package com.app.distribution.movie_biz.entites;

import java.util.Objects;

public class PaymentInformation {
	private PaymentDetails data;
	private boolean success;
	private String code;
	private String message;

	public PaymentDetails getData() {
		return data;
	}

	public void setData(PaymentDetails data) {
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, data, message, success);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PaymentInformation other = (PaymentInformation) obj;
		return Objects.equals(code, other.code) && Objects.equals(data, other.data)
				&& Objects.equals(message, other.message) && success == other.success;
	}

	@Override
	public String toString() {
		return "PaymentInformation [data=" + data + ", success=" + success + ", code=" + code + ", message=" + message
				+ "]";
	}

	public PaymentInformation() {
		super();
	}
}