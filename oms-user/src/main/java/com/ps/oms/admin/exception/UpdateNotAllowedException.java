package com.ps.oms.admin.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(value=HttpStatus.FORBIDDEN)
@Getter
public class UpdateNotAllowedException extends Exception{

	private static final long serialVersionUID = 1L;
	private final String message;
	public UpdateNotAllowedException(Integer userId, Boolean status) {
	
		this.message = String.format("Update not allowed on status with UserId %d for status  %b",userId,status);
	}
	
}

