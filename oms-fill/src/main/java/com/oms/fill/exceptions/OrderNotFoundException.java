package com.oms.fill.exceptions;

public class OrderNotFoundException extends Exception {

	private static final long serialVersionUID = 2L;

	public OrderNotFoundException(String errorMessage) {
		super(errorMessage);
	}
}