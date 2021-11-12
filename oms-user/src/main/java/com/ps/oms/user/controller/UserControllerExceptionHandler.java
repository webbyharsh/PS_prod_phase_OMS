package com.ps.oms.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ps.oms.user.dto.ErrorResponse;
import com.ps.oms.user.exceptions.PasswordUpdateException;
import com.ps.oms.user.exceptions.ResourceNotFoundException;
import com.ps.oms.user.exceptions.UpdateNotAllowedException;
import com.ps.oms.user.exceptions.UserException;
import com.ps.oms.user.exceptions.VerificationFailedException;

import lombok.extern.slf4j.Slf4j;

// Class to handle exceptions
@Slf4j
@ControllerAdvice
public class UserControllerExceptionHandler {

	// custom exception - handle exception if user exists
	@ExceptionHandler(UserException.class)
	public ResponseEntity<ErrorResponse> handleException(UserException ueEx) {
		log.error("User Exception Thrown : "+ ueEx.getMessage());
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.toString(), ueEx.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	}

	// custom exception - handle exception if problem updating or resetting password
	@ExceptionHandler(PasswordUpdateException.class)
	public ResponseEntity<ErrorResponse> handleException(PasswordUpdateException puEx) {
		log.error("Password Update Exception Thrown : " + puEx.getMessage());
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.toString(), puEx.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
	}

	// custom exception - handle exception if verification failed
	@ExceptionHandler(VerificationFailedException.class)
	public ResponseEntity<ErrorResponse> handleException(VerificationFailedException vfEx) {
		log.error("Verification Failed Exception Thrown : " + vfEx.getMessage());
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.toString(), vfEx.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
	}

	// handle exception if the request is bad
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException manvEx) {
		log.error("Method Argument Not Valid Exception Thrown : " + manvEx.getMessage());

		final StringBuilder errors = new StringBuilder();
		errors.append("Argument Validation Error \n");

		for (final FieldError error : manvEx.getBindingResult().getFieldErrors()) {
			errors.append("\n");
			errors.append(error.getField() + " : " + error.getDefaultMessage());
		}

		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), errors.toString());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	// handle exception if the request is bad
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleException(HttpMessageNotReadableException hmnrEx) {
		log.error("Http Message Not Readable Exception : " + hmnrEx.getMessage());
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Malformed JSON request");
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	// handle exception if server error
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		log.error("Generic Exception Thrown : " + ex.getLocalizedMessage());
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
				ex.getLocalizedMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> handleException(ResourceNotFoundException ex) {
		log.error("404  No detail found for given user");
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UpdateNotAllowedException.class)
	public ResponseEntity<String> handleException(UpdateNotAllowedException ex) {
		log.error("403 update not allowed");
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
	}
}
