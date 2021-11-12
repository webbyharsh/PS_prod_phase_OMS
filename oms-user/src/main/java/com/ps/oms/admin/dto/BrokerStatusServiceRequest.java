package com.ps.oms.admin.dto;


import javax.validation.constraints.NotNull;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BrokerStatusServiceRequest {

	@NotNull
	private String userId;
	
	@NotNull
	private String isActive;
}
