package com.ps.oms.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.ps.oms.user.entities.Address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserRequest {

	@NotNull
	private String name;
	
	@Email(message = "Email should be Valid")
	private String emailId;
	
	
	/*
	A password is considered valid if all the following constraints are satisfied:
		It contains at least 8-16 characters.
		It contains at least one digit.
		It contains at least one upper case alphabet.
		It contains at least one lower case alphabet.
		It contains at least one special character
		It doesn’t contain any white space.
	*/
	
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$", message = "Password must contain 8-16 characters, 1 lowercase, 1 uppercase, 1 digit, 1 special character and no white spaces.")
	private String password;
	
	@NotNull
	@Pattern(regexp="(^$|[0-9]{10})")
	private String contactNumber;
	
	@NotNull
	private Address address;
	
	@NotNull
	private Integer age;
	
}