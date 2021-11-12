package com.ps.oms.user.exceptions;

public class VerificationFailedException extends Exception {

	private static final long serialVersionUID = 3L;

	public VerificationFailedException(String errorMessage) {
		super(errorMessage);
	}
}