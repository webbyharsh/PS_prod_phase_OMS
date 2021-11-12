package com.ps.oms.user.controller;

import java.net.URI;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ps.oms.user.dto.UserDetailResponse;
import com.ps.oms.user.dto.UserUpdateRequest;
import com.ps.oms.user.dto.UserUpdateResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestController_2 {

	private TestRestTemplate restTemplate = new TestRestTemplate();
	final String url = "http://localhost:9002/api/v1/user-profile/";
	private HttpHeaders headers = null;
	URI targetUrl = null;
	URI targetUrl_2 = null;
	UserUpdateRequest userRequest = null;
	UserUpdateRequest userRequest_2 = null;
	Integer userId = null;
	Integer userId_2 = null;

	@Test(enabled = false)
	public void viewApiNotFoundResponse() {
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<UserDetailResponse> response = this.restTemplate.exchange(targetUrl, HttpMethod.GET, entity,
				UserDetailResponse.class);
		UserDetailResponse userResponse = response.getBody();
		Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
		Assert.assertTrue(userResponse.equals("User not found with id: '100'"));
	}

	@Test(enabled = false)
	public void viewApiUnauthorized() {

		headers.set("Authorization", null);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<UserDetailResponse> response = this.restTemplate.exchange(targetUrl, HttpMethod.GET, entity,
				UserDetailResponse.class);
		Assert.assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
		Assert.assertEquals(response.getBody(), null);
		headers.set("Authorization", userId.toString());
	}

	@Test(enabled = false)
	public void updateApiNotFoundResponse() {
		HttpEntity<UserUpdateRequest> entity = new HttpEntity<>(userRequest, headers);
		ResponseEntity<UserUpdateResponse> response = this.restTemplate.exchange(targetUrl, HttpMethod.PUT, entity,
				UserUpdateResponse.class);
		Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
		Assert.assertTrue(response.getBody().getResponseBody().equals("User not found with id: '100'"));
	}

	@Test(enabled = false)
	public void updateApiUnauthorized() {

		headers.set("Authorization", null);
		HttpEntity<UserUpdateRequest> entity = new HttpEntity<>(userRequest, headers);
		ResponseEntity<UserUpdateResponse> response = this.restTemplate.exchange(targetUrl, HttpMethod.PUT, entity,
				UserUpdateResponse.class);
		Assert.assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
		Assert.assertEquals(response.getBody(), null);
		headers.set("Authorization", userId.toString());
	}

	@Test(enabled = false)
	public void updateApiEmailUpdateUnauthorized() {
		userRequest.setEmailId("s@gmail.com");
		HttpEntity<UserUpdateRequest> entity = new HttpEntity<>(userRequest, headers);
		ResponseEntity<UserUpdateResponse> response = this.restTemplate.exchange(targetUrl_2, HttpMethod.PUT, entity,
				UserUpdateResponse.class);
		Assert.assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
		Assert.assertTrue(response.getBody().getResponseBody().equals("email update not allowed on User with id: '1'"));
		userRequest.setEmailId("sounak.1999@gmail.com");
	}

	@Test(enabled = false)
	public void updateApiUserIdRoleIdUpdateUnauthorized() {
		userRequest_2.setUserId(2L);
		HttpEntity<UserUpdateRequest> entity = new HttpEntity<>(userRequest_2, headers);
		ResponseEntity<UserUpdateResponse> response = this.restTemplate.exchange(targetUrl_2, HttpMethod.PUT, entity,
				UserUpdateResponse.class);
		Assert.assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
		Assert.assertEquals(response.getBody().getResponseBody(),
				"userId and roleId update not allowed on User with id: '1'");
		Assert.assertTrue(response.getBody().getResponseBody()
				.equals("userId and roleId update not allowed on User with id: '1'"));
		userRequest_2.setUserId(1L);
	}

	@BeforeClass
	public void setup() {
		userId = 100;
		userId_2 = 1;
		targetUrl = UriComponentsBuilder.fromUriString(url + userId).build().encode().toUri();
		targetUrl_2 = UriComponentsBuilder.fromUriString(url + userId_2).build().encode().toUri();
		headers = new HttpHeaders();
		headers.set("Authorization", userId.toString());
		userRequest = new UserUpdateRequest();
		userRequest.setUserId(1L);
		userRequest.setName("s");
		//userRequest.setAddress("bangalore");
		userRequest.setAge(21);
		userRequest.setContact("2553679");
		userRequest.setEmailId("sounak@gmail.com");
		userRequest_2 = new UserUpdateRequest();
		userRequest_2.setUserId(1L);
		userRequest_2.setName("s");
		//userRequest_2.setAddress("bangalore");
		userRequest_2.setAge(21);
		userRequest_2.setContact("2553679");
		userRequest_2.setEmailId("sounak@gmail.com");
	}
}
