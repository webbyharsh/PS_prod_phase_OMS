package com.ps.oms.user.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ps.oms.user.dto.UserDetailResponse;
import com.ps.oms.user.dto.UserUpdateRequest;
import com.ps.oms.user.dto.UserUpdateResponse;
import com.ps.oms.user.entities.User;
import com.ps.oms.user.exceptions.ResourceNotFoundException;
import com.ps.oms.user.exceptions.UpdateNotAllowedException;
import com.ps.oms.user.service.IUserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class UserController {

	@Autowired
	IUserService userService;
	

	@ApiOperation("Get User details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 400, message = "BAD REQUEST"),
			@ApiResponse(code = 404, message = "NOT FOUND"), @ApiResponse(code = 403, message = "FORBIDDEN"),
			@ApiResponse(code = 500, message = "SERVER ERROR") })
	@GetMapping("/getDetails")
	@CrossOrigin
	public ResponseEntity<UserDetailResponse> userDetailsView(@RequestParam("user_id") Long userId,
			@RequestHeader("Authorization") String jwt) throws ResourceNotFoundException {
		if (!jwt.isEmpty()) {

			User user = userService.getUserDetails(userId);
			log.info(user.toString());
			log.info("200 User detail fetch is successful");
			UserDetailResponse userDetails = new UserDetailResponse(user);
			return ResponseEntity.status(HttpStatus.OK).body(userDetails);
		}

		log.error("401 Unauthorized Error");
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	@ApiOperation("Update user details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "ok", response = UserUpdateResponse.class),
			@ApiResponse(code = 404, message = "not found", response = UserUpdateResponse.class),
			@ApiResponse(code = 403, message = "forbidden", response = UserUpdateResponse.class),
			@ApiResponse(code = 500, message = "server error", response = UserUpdateResponse.class) })
	@PutMapping("/updateDetails")
	@CrossOrigin
	public ResponseEntity<String> updateUser(
			@RequestBody @Valid UserUpdateRequest newUserDetailRequest, @RequestHeader("Authorization") String jwt) throws ResourceNotFoundException, UpdateNotAllowedException, JsonProcessingException {

		if (!jwt.isEmpty()) {
			Long userId = newUserDetailRequest.getUserId();
			userService.updateUser(newUserDetailRequest, userId);
		
			log.info("Update profile notification calling");
			userService.updateProfileChangeNotificationService(newUserDetailRequest);
			log.info("Update profile notification service called");
			log.info("user detail updated");
			return new ResponseEntity<>(HttpStatus.OK);
		}

		log.error("401 unauthorized request");
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

}
