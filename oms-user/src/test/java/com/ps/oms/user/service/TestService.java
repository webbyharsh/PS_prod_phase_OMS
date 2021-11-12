package com.ps.oms.user.service;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ps.oms.user.dto.UserDetailResponse;
import com.ps.oms.user.dto.UserUpdateRequest;
import com.ps.oms.user.entities.User;
import com.ps.oms.user.exceptions.ResourceNotFoundException;
import com.ps.oms.user.exceptions.UpdateNotAllowedException;

@SpringBootTest
public class TestService extends AbstractTestNGSpringContextTests{
	
	@Autowired
	private IUserService userService; 
	User expectedUser = null;
	UserDetailResponse expectedDetail = null;
	UserUpdateRequest userRequest = null;
	LocalDateTime dateTime = null;
	String str = "2021-07-29T17:01:53.405373";
	@Test
	public void getUser() {
		User user = null;
		try {
			user = userService.getUserDetails(1L);
			
			
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		}
		UserDetailResponse userDetails = new UserDetailResponse(expectedUser);
		System.out.print("user dt=etails ============================================================---" + userDetails.getEmailId());
		Assert.assertNotEquals(user, expectedUser);
		Assert.assertNotNull(userDetails);
		;
	}
	@Test(enabled=false)
	public void updateUser() {
		
		try {
			userService.updateUser(userRequest, 1L);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		} catch (UpdateNotAllowedException e) {
			e.printStackTrace();
		}
		
		User actualUser = null;
		try {
			actualUser = userService.getUserDetails(1L);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		}
		
		expectedUser.setName("snk");
		expectedUser.setAge(20);
		//expectedUser.setAddress("bangalore");
		expectedUser.setContact("255367889");
		
		Assert.assertEquals(actualUser, expectedUser);
	}
	@BeforeClass
	public void setup() {
		dateTime = LocalDateTime.parse(str, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		expectedUser = new User();
		expectedUser.setName("sounak");
		expectedUser.setUserId(1L);
		expectedUser.setAge(22);
		expectedUser.setActive(true);
		expectedUser.setContact("2553678");
		expectedUser.setEmailId("sounak@gmail.com");
		expectedUser.setPassword("XXXX");
		expectedUser.setCreatedAt(dateTime);
		expectedUser.setLastActiveAt(dateTime);
		
		userRequest = new UserUpdateRequest();
		userRequest.setUserId(1L);
		userRequest.setName("snk");
		userRequest.setAge(20);
		userRequest.setContact("255367889");
		userRequest.setEmailId("sounak@gmail.com");
	}
	
	@AfterClass
	public void tearDown() {
		
		userRequest.setName("sounak");
		userRequest.setAge(22);
		userRequest.setContact("2553678");
		
		try {
			userService.updateUser(userRequest, 1L);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		} catch (UpdateNotAllowedException e) {
			e.printStackTrace();
		}
	}
}
