package com.ps.oms.user.exceptions;

public class PasswordUpdateException extends Exception {

	private static final long serialVersionUID = 2L;

	public PasswordUpdateException(String errorMessage) {
		super(errorMessage);
	}
}