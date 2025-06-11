package com.app.distribution.movie_biz.exceptions;

public class InvalidBankDetailsException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidBankDetailsException() {
	}

	public InvalidBankDetailsException(String message) {
		super(message);
	}
}