package com.ps.oms.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class ErrorResponse {

	private String error;

	private String message;
}
