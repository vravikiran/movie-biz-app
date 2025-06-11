package com.app.distribution.movie_biz.exceptions;

public class InvalidKycDetailsException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidKycDetailsException() {
		super();
	}

	public InvalidKycDetailsException(String message) {
		super(message);
	}

}