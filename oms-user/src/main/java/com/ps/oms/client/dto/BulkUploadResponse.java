package com.ps.oms.client.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BulkUploadResponse implements IBulkUploadResponse {
	
	private String message;

	private Integer total;
	
	private Integer successful;
	
	private Map<Integer, String> failures;

}
