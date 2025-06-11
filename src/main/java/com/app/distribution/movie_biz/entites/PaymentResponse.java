package com.app.distribution.movie_biz.entites;

public class PaymentResponse {
	private String response;

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public PaymentResponse() {
		super();
	}

	@Override
	public String toString() {
		return "PaymentResponse [response=" + response + "]";
	}
}