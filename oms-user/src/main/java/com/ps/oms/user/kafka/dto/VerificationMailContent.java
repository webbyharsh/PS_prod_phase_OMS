package com.ps.oms.user.kafka.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VerificationMailContent {
	
	private String template;
	
	private String siteURL;
	
	private String emailId;
	
	private String name;
	
	private String verificationCode;

}
