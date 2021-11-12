package com.ps.oms.client.controller;



import javax.validation.UnexpectedTypeException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.ps.oms.client.dto.ErrorResponse;
import com.ps.oms.client.exceptions.IncorrectCsvHeaderException;

import lombok.extern.slf4j.Slf4j;

//Handles different exceptions
@Slf4j
@ControllerAdvice
public class ClientControllerExceptionHandler {
	
	@ExceptionHandler(MultipartException.class)
	public ResponseEntity<ErrorResponse> handleException(MultipartException mpEx) {
		log.error("Multipart Exception thrown");
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), mpEx.getLocalizedMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	

	@ExceptionHandler(MissingServletRequestPartException.class)
	public ResponseEntity<ErrorResponse> handleException(MissingServletRequestPartException msrpEx) {
		log.error("Missing Servlet Request Part Exception thrown");
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), msrpEx.getLocalizedMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	
	

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ErrorResponse> handleException(MaxUploadSizeExceededException museEx) {
		log.error("Max Upload Size ExceededException thrown");
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.PAYLOAD_TOO_LARGE.toString(), museEx.getLocalizedMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.PAYLOAD_TOO_LARGE);
	}

	
	
	@ExceptionHandler(UnexpectedTypeException.class)
	public ResponseEntity<ErrorResponse> handleException(UnexpectedTypeException utEx) {
		log.error("Unexpected Type Exception thrown");
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE.toString(),
				utEx.getLocalizedMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}

	
	
	@ExceptionHandler(IncorrectCsvHeaderException.class)
	public ResponseEntity<ErrorResponse> handleException(IncorrectCsvHeaderException ichEx) {
		log.error("Incorrect CsvHeader Exception thrown");
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), ichEx.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	
	
	// handle exception if missing header
	@ExceptionHandler(MissingRequestHeaderException.class)
	public ResponseEntity<ErrorResponse> handleException(MissingRequestHeaderException mrhEx) {
		log.error("Missing Request Header Exception Thrown");
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), mrhEx.getLocalizedMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	
	
	// handle exception if header incorrect
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleException(MethodArgumentTypeMismatchException matmEx) {
		log.error("Method Argument Type Mismatch Exception Thrown");
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), "Malformed Header");
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

}
