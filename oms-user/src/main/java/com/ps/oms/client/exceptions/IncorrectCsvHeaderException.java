package com.ps.oms.client.exceptions;

public class IncorrectCsvHeaderException extends Exception {

	private static final long serialVersionUID = 1L;

	public IncorrectCsvHeaderException(String errorMessage) {
		super(errorMessage);
	}
}