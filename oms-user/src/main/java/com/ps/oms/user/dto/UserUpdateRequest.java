package com.ps.oms.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.ps.oms.user.entities.Address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserUpdateRequest {

	@NotNull
	private Long userId;
    
	@NotNull
	private String name;
	
	@NotNull
	private Address address;
	
	@NotNull
	@Pattern(regexp="(^$|[0-9]{10})")
	@Size(max = 12)
	private String contact;
	
	@NotNull
	@Email(message = "Email Address check format")
	@Size(max = 100)
	private String emailId;
	
	@NotNull
	private Integer age;
}
