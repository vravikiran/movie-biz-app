package com.app.distribution.movie_biz.exceptions;

public class InvalidTransactionStatusException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidTransactionStatusException() {
		super();
	}

	public InvalidTransactionStatusException(String message) {
		super(message);
	}

}