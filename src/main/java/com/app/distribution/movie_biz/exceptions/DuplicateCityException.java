package com.app.distribution.movie_biz.exceptions;

public class DuplicateCityException extends Exception {

	private static final long serialVersionUID = 1L;

	public DuplicateCityException() {
		super();
	}

	public DuplicateCityException(String message) {
		super(message);
	}

}
