package com.ps.oms.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BulkUploadErrorResponse implements IBulkUploadResponse {

	private String message;

	private Integer total;
	
	private Integer validRowCount;
	
	private Integer errorRowCount;
	
	private ErrorGroup errors;

}
