package com.app.distribution.movie_biz.exceptions;

public class InvalidInvestmentStatusException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidInvestmentStatusException() {
		super();
	}

	public InvalidInvestmentStatusException(String message) {
		super(message);
	}
}