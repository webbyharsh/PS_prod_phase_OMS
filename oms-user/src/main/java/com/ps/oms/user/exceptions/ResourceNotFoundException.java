package com.ps.oms.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;


@ResponseStatus(value=HttpStatus.NOT_FOUND)
@Getter
public class ResourceNotFoundException extends Exception{

	private static final long serialVersionUID = 1L;
	private final String entityName;
	private final String primaryId;
	private final transient Object field;
	private final String message;
	public ResourceNotFoundException(String entityName, String primaryId, Object field) {
		super(String.format("%s not found with %s: '%s'", entityName, primaryId, field));
		this.entityName = entityName;
		this.primaryId = primaryId;
		this.field = field;
		this.message = String.format("%s not found with %s: '%s'", entityName, primaryId, field);

}
}
