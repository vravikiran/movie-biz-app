package com.app.distribution.movie_biz.exceptions;

public class TheatreNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public TheatreNotFoundException(String message) {
		super(message);
	}

	public TheatreNotFoundException() {
		super();
	}
}
