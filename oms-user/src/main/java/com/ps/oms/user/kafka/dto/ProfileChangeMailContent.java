package com.ps.oms.user.kafka.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProfileChangeMailContent {
	

	private Long userId;
    
	
	private String name;
	
	
	private String address;
	
	
	@Pattern(regexp="(^$|[0-9]{10})")
	@Size(max = 12)
	private String contact;
	
	
	@Email(message = "Email Address check format")
	@Size(max = 100)
	private String emailId;
	
	
	private Integer age;

	
	private String template;
	
}
