package com.ps.oms.user.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ResetPasswordRequest {
	
	private String token;

	/*
	A password is considered valid if all the following constraints are satisfied:
		It contains at least 8-16 characters.
		It contains at least one digit.
		It contains at least one upper case alphabet.
		It contains at least one lower case alphabet.
		It contains at least one special character
		It doesn’t contain any white space.
	*/
	
	@NotNull(message = "New password cannot be empty")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$", message = "Password must contain 8-16 characters, 1 lowercase, 1 uppercase, 1 digit, 1 special character and no white spaces.")
	private String newPassword;
	
	@NotNull(message = "Confirm password cannot be empty")
	private String confirmPassword;
	
}