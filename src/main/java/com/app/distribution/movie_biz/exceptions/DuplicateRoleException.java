package com.app.distribution.movie_biz.exceptions;

public class DuplicateRoleException extends Exception {

	private static final long serialVersionUID = 1L;

	public DuplicateRoleException() {
		super();
	}

	public DuplicateRoleException(String message) {
		super(message);
	}

}
