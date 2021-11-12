package com.ps.oms.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.FORBIDDEN)
public class UpdateNotAllowedException extends Exception{

	private static final long serialVersionUID = 1L;
	private final String entityName;
	private final String primaryId;
	private final transient Object field;
	private final String message;
	public UpdateNotAllowedException(String type, String entityName, String primaryId, Object field) {
		super(String.format("%s update not allowed on %s with %s: '%s'", type, entityName, primaryId, field));
		this.entityName = entityName;
		this.primaryId = primaryId;
		this.field = field;
		this.message = String.format("%s update not allowed on %s with %s: '%s'", type, entityName, primaryId, field);
	}
	public String getEntityName() {
		return entityName;
	}
	public String getPrimaryId() {
		return primaryId;
	}
	public Object getField() {
		return field;
	}
	@Override
	public String getMessage() {
		return message;
	}
}
