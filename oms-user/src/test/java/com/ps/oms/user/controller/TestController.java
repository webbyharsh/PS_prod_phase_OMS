package com.ps.oms.user.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ps.oms.user.dto.UserDetailResponse;
import com.ps.oms.user.dto.UserUpdateRequest;
import com.ps.oms.user.dto.UserUpdateResponse;
import com.ps.oms.user.entities.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestController {

	private RestTemplate restTemplate = new RestTemplate();
	LocalDateTime dateTime = null;
	final String str = "2021-07-29T17:01:53.405373";
	final String url = "http://localhost:9002/api/v1/user-profile/1";
	private HttpHeaders headers = null;
	URI targetUrl = null;
	User expectedUser = null;
	User actualUser = null;
	UserUpdateRequest userRequest = null;

	@Test(enabled = false)
	public void getUserApi() {

		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<UserDetailResponse> response = this.restTemplate.exchange(targetUrl, HttpMethod.GET, entity,
				UserDetailResponse.class);
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
		UserDetailResponse userResponse = response.getBody();
		Assert.assertEquals(userResponse, new UserDetailResponse(expectedUser));
		Assert.assertTrue(
				userResponse.toString().contains("UserServiceResponse [responseBody=Success, response=User ["));
	}

	@Test(enabled = false)
	public void updateUserDetailApi() {

		HttpEntity<UserUpdateRequest> entity = new HttpEntity<>(userRequest, headers);
		ResponseEntity<UserUpdateResponse> response = this.restTemplate.exchange(targetUrl, HttpMethod.PUT, entity,
				UserUpdateResponse.class);
		UserUpdateResponse userResponse = response.getBody();

		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
		Assert.assertEquals(userResponse.getResponseBody(), "Success");
		Assert.assertTrue(userResponse.toString().equals("UserUpdateResponse [responseBody=Success]"));

		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<UserDetailResponse> res = this.restTemplate.exchange(targetUrl, HttpMethod.GET, request,
				UserDetailResponse.class);

		// expectedUser.setAddress("bangalore");
		expectedUser.setName("s");
		expectedUser.setAge(21);
		// expectedUser.setContact("2553679");

		Assert.assertEquals(res.getBody(), new UserDetailResponse(expectedUser));
	}

	@BeforeClass
	public void setup() {
		dateTime = LocalDateTime.parse(str, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		targetUrl = UriComponentsBuilder.fromUriString(url).build().encode().toUri();
		headers = new HttpHeaders();
		headers.set("Authorization", "1");
		expectedUser = new User();
		expectedUser.setName("sounak");
		expectedUser.setUserId(1L);
		expectedUser.setAge(22);
		// expectedUser.setAddress("gurgaon");
		expectedUser.setActive(true);
		expectedUser.setContact("2553678");
		expectedUser.setEmailId("sounak@gmail.com");
		expectedUser.setPassword("XXXX");
		expectedUser.setCreatedAt(dateTime);
		expectedUser.setLastActiveAt(dateTime);

		userRequest = new UserUpdateRequest();
		userRequest.setUserId(1L);
		userRequest.setName("s");
		// userRequest.setAddress("bangalore");
		userRequest.setAge(21);
		userRequest.setContact("2553679");
		userRequest.setEmailId("sounak@gmail.com");

	}

	@AfterClass
	public void cleanup() {

		// userRequest.setAddress("gurgaon");
		userRequest.setName("sounak");
		userRequest.setAge(22);
		userRequest.setContact("2553678");

		HttpEntity<UserUpdateRequest> entity = new HttpEntity<>(userRequest, headers);
		this.restTemplate.exchange(targetUrl, HttpMethod.PUT, entity, UserUpdateResponse.class);
	}
}