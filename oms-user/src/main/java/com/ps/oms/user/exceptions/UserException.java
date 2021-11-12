package com.ps.oms.user.exceptions;

public class UserException extends Exception {

	private static final long serialVersionUID = 2L;

	public UserException(String errorMessage) {
		super(errorMessage);
	}
}