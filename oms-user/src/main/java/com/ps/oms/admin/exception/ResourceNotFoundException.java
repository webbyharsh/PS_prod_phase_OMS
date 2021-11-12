package com.ps.oms.admin.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import lombok.Getter;

@ResponseStatus(value=HttpStatus.NOT_FOUND)
@Getter
public class ResourceNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	private final String message;
	public ResourceNotFoundException(String entityName, String primaryId, Object field) 
	{
		
		this.message = String.format("%s not found with %s: '%s'", entityName, primaryId, field);
	}

}
