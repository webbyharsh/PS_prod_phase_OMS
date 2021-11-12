package com.oms.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.oms.order.dto.ErrorResponse;
import com.oms.order.exceptions.BadRequestException;
import com.oms.order.exceptions.OrderNotFoundException;

import lombok.extern.slf4j.Slf4j;

// Class to handle exceptions
@ControllerAdvice
@Slf4j
public class OrderControllerExceptionHandler {

	// custom exception - handle exception if request data is incorrect
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> handleException(BadRequestException brEx) {
		log.error("Bad Request Exception Thrown");
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), brEx.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	// custom exception - handle exception if order id is incorrect
	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleException(OrderNotFoundException onfEx) {
		log.error("Order Not Found Exception Thrown");
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.OK.toString(), onfEx.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.OK);
	}

	// handle exception if the request is bad
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException manvEx) {
		log.error("Method Argument Not Valid Exception Thrown");
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(),
				"One or more fields are not present");
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	// handle exception if the request is bad
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleException(HttpMessageNotReadableException hmnrEx) {
		log.error("Http Message Not Readable Exception Thrown");
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Malformed JSON request");
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	// handle exception if missing header
	@ExceptionHandler(MissingRequestHeaderException.class)
	public ResponseEntity<ErrorResponse> handleException(MissingRequestHeaderException mrhEx) {
		log.error("Missing Request Header Exception Thrown");
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(),
				mrhEx.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	// handle exception if header incorrect
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleException(MethodArgumentTypeMismatchException matmEx) {
		log.error("Method Argument Type Mismatch Exception Thrown");
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Malformed Header");
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	// handle exception if error
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		log.error("Exception Thrown");
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ex.getLocalizedMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}