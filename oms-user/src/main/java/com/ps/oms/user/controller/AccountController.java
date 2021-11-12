package com.ps.oms.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ps.oms.user.dto.ResetPasswordRequest;
import com.ps.oms.user.dto.UpdatePasswordRequest;
import com.ps.oms.user.dto.UserRequest;
import com.ps.oms.user.dto.UserResponse;
import com.ps.oms.user.exceptions.PasswordUpdateException;
import com.ps.oms.user.exceptions.UserException;
import com.ps.oms.user.exceptions.VerificationFailedException;
import com.ps.oms.user.service.IUserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class AccountController {

	@Autowired
	IUserService userService;
	

	@PostMapping("/register")
	@ResponseBody
	public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest userRequest,
			HttpServletRequest request) throws JsonProcessingException, UserException {

		log.info("RegisterUser of Controller Called");

		userService.register(userRequest, getSiteURL(request));
		UserResponse userResponse = new UserResponse(HttpStatus.ACCEPTED.toString(),
				"We have sent you a verification email. Click on it to enable you account.");

		return new ResponseEntity<>(userResponse, HttpStatus.ACCEPTED);
	}

	@GetMapping("/verify")
	public String verifyUser(@Param("code") String code, Model model, HttpServletResponse response) {

		log.info("VerifyUser of Controller Called");
		int codeValidity = userService.verify(code);
		
		log.info("Validity of verification code : " + codeValidity);
		
		if (codeValidity == -1) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			model.addAttribute("message", "The token is invalid or expired");
			return "registration_failed";
		} 
		
//		else if (codeValidity == 0) {
//			response.setStatus(HttpStatus.UNAUTHORIZED.value());
//			model.addAttribute("message", "The activation token expired and cannot be used");
//			return "registration_failed";
//		}

		return "registration_success";
	}

	

	@PostMapping("/forgot-password")
	@ResponseBody
	public ResponseEntity<UserResponse> handleForgotPassword(@RequestParam("emailId") String email,
			HttpServletRequest request) throws PasswordUpdateException, JsonProcessingException, UserException {

		log.info("handleResetPassword of Controller Called");
		userService.sendResetPasswordEmail(email, getSiteURL(request));
		log.info("UserService: sendResetPasswordEmail execution complete and returning response.");

		UserResponse userResponse = new UserResponse(HttpStatus.ACCEPTED.toString(),
				"A password reset link has been sent to your mail.");

		return new ResponseEntity<>(userResponse, HttpStatus.ACCEPTED);
	}
	
	
	
	@GetMapping("/reset-password")
	public String showResetPasswordForm(@Param(value = "token") String token, Model model, HttpServletResponse response) {
		
		log.info("reset-password GET API called");
		int tokenValidity = userService.resetPasswordTokenIsValid(token);
		
		log.info("Reset Password Token Validity : " + tokenValidity);
		
		if (tokenValidity == -1) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			model.addAttribute("message", "The token is either invalid, expired or already used");
			return "token_validation_failed";
			
		} else if (tokenValidity == 0) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			model.addAttribute("message", "The token expired and cannot be used");
			return "token_validation_failed";
		}
		
		
		model.addAttribute("token", token);
		return "reset_password_form";

	}

	
	
	@PostMapping("/reset-password")
	public ResponseEntity<UserResponse> handleResetPassword(
			@Valid @RequestBody ResetPasswordRequest resetPasswordRequest)
			throws PasswordUpdateException, JsonProcessingException {

		log.info("handleResetPassword of Controller Called");

		userService.resetPassword(resetPasswordRequest);
		log.info("UserService: resetPassword execution complete and returning response.");

		UserResponse userResponse = new UserResponse(HttpStatus.OK.toString(), "Password Successfully Reset.");

		return new ResponseEntity<>(userResponse, HttpStatus.OK);
	}

	
	
	
	@PostMapping("/update-password")
	@ResponseBody
	public ResponseEntity<UserResponse> handleUpdatePassword(
			@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest, @RequestHeader("userId") Long userId) throws PasswordUpdateException, VerificationFailedException {

		log.info("handleUpdatePassword of Controller Called");

		userService.updatePassword(updatePasswordRequest, userId);
		log.info("UserService: sendPesetPasswordEmail execution complete and returning response.");

		UserResponse userResponse = new UserResponse(HttpStatus.OK.toString(), "Password Successfully Updated.");

		return new ResponseEntity<>(userResponse, HttpStatus.OK);
	}

	
	
	private String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}
	
	
	@PostMapping("/delete")
	public ResponseEntity<UserResponse> deleteUser(@RequestParam("emailId") String emailId) {
		userService.deleteUser(emailId);
		UserResponse userResponse = new UserResponse(HttpStatus.ACCEPTED.toString(),
				"User Deleted if Existed");

		return new ResponseEntity<>(userResponse, HttpStatus.ACCEPTED);
		
	}
	
	@PostMapping("/delete-password-token")
	public ResponseEntity<UserResponse> deletePasswordToken(@RequestParam("emailId") String emailId) {
		userService.deletePasswordToken(emailId);
		UserResponse userResponse = new UserResponse(HttpStatus.ACCEPTED.toString(),
				"Password Token Deleted if Existed for corresponding User");

		return new ResponseEntity<>(userResponse, HttpStatus.ACCEPTED);
		
	}
	
	
}