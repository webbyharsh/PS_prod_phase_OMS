package com.ps.oms.user.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.ps.oms.user.dto.ResetPasswordRequest;
import com.ps.oms.user.dto.UserRequest;
import com.ps.oms.user.exceptions.UserException;
import com.ps.oms.user.exceptions.VerificationFailedException;
import com.ps.oms.user.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@TestExecutionListeners(MockitoTestExecutionListener.class)
class UserRegistrationControllerUsingMocksTests extends AbstractTestNGSpringContextTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;
	
	@AfterMethod
	void reset_mocks() {
	    Mockito.reset(userService);
	}

	// If request body is of correct format return response with status 202
	@Test
	void test_register_user_and_status_accepted_if_request_ok() throws Exception {
		String postUserJSON = "{\"name\":\"Test Name\", \"emailId\":\"test@name.com\", \"password\":\"testNAME@123\", \"age\": 20, \"contactNumber\": \"1234567890\", \"address\": {\"country\": \"Test Country\", \"city\": \"Test City\", \"state\": \"Test State\", \"street\": \"Test Street\"}}";

		// Mock the service layer
		// If service layer is called true will be returned without calling service
		Mockito.when(userService.register(Mockito.isA(UserRequest.class), Mockito.anyString())).thenReturn(true);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/register")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(postUserJSON);

		MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();

		assertEquals(response.getStatus(), HttpStatus.ACCEPTED.value());

	}

	// Status ok if user already exists
	@Test
	void test_status_conflict_if_user_exists() throws Exception {
		String postUserJSON = "{\"name\":\"Test Name\", \"emailId\":\"test@name.com\", \"password\":\"testNAME@123\", \"age\": 20, \"contactNumber\": \"1234567890\", \"address\": {\"country\": \"Test Country\", \"city\": \"Test City\", \"state\": \"Test State\", \"street\": \"Test Street\"}}";

		// Mock the service layer
		// If service layer is called exception will be thrown
		Mockito.when(userService.register(Mockito.isA(UserRequest.class), Mockito.anyString()))
				.thenThrow(new UserException("Test ex"));

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/register")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(postUserJSON);

		MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();

		assertEquals(response.getStatus(), HttpStatus.CONFLICT.value());
	}

	// Internal server error if error in service
	@Test
	void test_server_error_if_exception_in_service() throws Exception {
		String postUserJSON = "{\"name\":\"Test Name\", \"emailId\":\"test@name.com\", \"password\":\"testNAME@123\", \"age\": 20, \"contactNumber\": \"1234567890\", \"address\": {\"country\": \"Test Country\", \"city\": \"Test City\", \"state\": \"Test State\", \"street\": \"Test Street\"}}";

		// Mock the service layer
		// If service layer is called exception will be thrown
		Mockito.when(userService.register(Mockito.isA(UserRequest.class), Mockito.anyString()))
				.thenThrow(new RuntimeException());

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/register")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(postUserJSON);

		MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();

		assertEquals(response.getStatus(), HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	// Throw Verification Failed Exception
	@Test
	void test_status_unauthorized_if_verification_failed() throws Exception {
		Mockito.when(userService.verify(Mockito.anyString())).thenAnswer(invocation -> {
			throw new VerificationFailedException("");
		});

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/verify").param("code", "1a2b3c");

		MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();

		assertEquals(response.getStatus(), HttpStatus.UNAUTHORIZED.value());
	}
	
	
	
	@Test
	void test_bad_request_if_json_not_correct() throws Exception {
		String postUserJSON = "{\"name\":Test Name, \"emailId\":\"test@name.com, \"password\":\"testNAME@123\", \"age\": 20, \"contactNumber\": \"1234567890\", \"address\": {\"country\": \"Test Country\", \"city\": \"Test City\", \"state\": \"Test State\", \"street\": \"Test Street\"}}";
		
		doNothing().when(userService).resetPassword(Mockito.isA(ResetPasswordRequest.class));
		
		
		mockMvc.perform(
				post("/api/v1/register")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(postUserJSON))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message", is("Malformed JSON request")));
	}
	
	
	
	// Test if /verify api return html
	@Test
	void test_verify_registration_html_response() throws Exception {
		Mockito.when(userService.verify(Mockito.anyString())).thenReturn(-1);

		mockMvc.perform(
				get("/api/v1/verify")
				.param("code", "mockCode"))
		.andExpect(status().isUnauthorized())
		.andExpect(content().contentType("text/html;charset=UTF-8"));
		
		Mockito.reset(userService);
		Mockito.when(userService.verify(Mockito.anyString())).thenReturn(1);

		mockMvc.perform(
				get("/api/v1/verify")
				.param("code", "mockCode"))
		.andExpect(status().isOk())
		.andExpect(content().contentType("text/html;charset=UTF-8"));
		
		Mockito.reset(userService);
		
	}
}
