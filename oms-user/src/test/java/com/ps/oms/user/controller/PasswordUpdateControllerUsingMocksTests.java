package com.ps.oms.user.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.ps.oms.user.dto.ResetPasswordRequest;
import com.ps.oms.user.dto.UpdatePasswordRequest;
import com.ps.oms.user.exceptions.PasswordUpdateException;
import com.ps.oms.user.exceptions.UserException;
import com.ps.oms.user.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@TestExecutionListeners(MockitoTestExecutionListener.class)
class PasswordUpdateControllerUsingMocksTests extends AbstractTestNGSpringContextTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;
	
	@AfterMethod
	void reset_mocks() {
	    Mockito.reset(userService);
	}
	
	
	@Test
	void test_forgot_password_status_accepted() throws Exception {

		doNothing().when(userService).sendResetPasswordEmail(Mockito.anyString(), Mockito.anyString());
		mockMvc.perform(post("/api/v1/forgot-password").param("emailId", "test@mail.com")).andExpect(status().isAccepted());
	}
	
	
	@Test
	void test_forgot_password_status_conflict() throws Exception {

		doThrow(new UserException("test")).when(userService).sendResetPasswordEmail(Mockito.anyString(), Mockito.anyString());
		mockMvc.perform(post("/api/v1/forgot-password").param("emailId", "test@mail.com")).andExpect(status().isConflict());
	}
	

	@Test
	void test_reset_password_status_ok() throws Exception {
		String postDtoJson = "{\"token\":\"abcd\", \"newPassword\":\"testPASS@123\", \"confirmPassword\":\"testPASS@123\"}";
		
		doNothing().when(userService).resetPassword(Mockito.isA(ResetPasswordRequest.class));
		
		
		mockMvc.perform(
				post("/api/v1/reset-password")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(postDtoJson))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.message", is("Password Successfully Reset.")));
	}
	
	
	@Test
	void test_reset_password_status_unauthorized() throws Exception {
		String postDtoJson = "{\"token\":\"abcd\", \"newPassword\":\"testPASS@123\", \"confirmPassword\":\"testPASS@123\"}";
		
		doThrow(new PasswordUpdateException("test")).when(userService).resetPassword(Mockito.isA(ResetPasswordRequest.class));
		
		mockMvc.perform(
				post("/api/v1/reset-password")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(postDtoJson))
		.andExpect(status().isUnauthorized());
	}
	
	
	@Test
	void test_reset_password_status_bad_request() throws Exception {
		//method argument not valid exception
		String postDtoJson = "{\"token\":\"abcd\", \"newPassword\":\"testPASSABC\", \"confirmPassword\":\"testPASSABC\"}";
		
		mockMvc.perform(
				post("/api/v1/reset-password")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(postDtoJson))
		.andExpect(status().isBadRequest());
	}
	
	
	@Test
	void test_update_password_status_unauthorized() throws Exception {
		String postDtoJson = "{\"oldPassword\":\"TESTpass@123\", \"newPassword\":\"testPASS@123\", \"confirmPassword\":\"testPASS@123\"}";
		
		doThrow(new PasswordUpdateException("test")).when(userService).updatePassword(Mockito.isA(UpdatePasswordRequest.class), Mockito.anyLong());
		
		mockMvc.perform(
				post("/api/v1/update-password")
				.header("userId", 12L)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(postDtoJson))
		.andExpect(status().isUnauthorized());
	}
	
	
	
	@Test
	void test_update_password_status_ok() throws Exception {
		String postDtoJson = "{\"oldPassword\":\"TESTpass@123\", \"newPassword\":\"testPASS@123\", \"confirmPassword\":\"testPASS@123\"}";
		
		doNothing().when(userService).updatePassword(Mockito.isA(UpdatePasswordRequest.class), Mockito.anyLong());
		
		mockMvc.perform(
				post("/api/v1/update-password")
				.header("userId", 12L)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(postDtoJson))
		.andExpect(status().isOk());
	}
	
	// test if reset-password get api returns html
	@Test
	void test_reset_password_form_html() throws Exception {
		Mockito.when(userService.resetPasswordTokenIsValid(Mockito.anyString())).thenReturn(-1);

		mockMvc.perform(
				get("/api/v1/reset-password")
				.param("token", "mockToken"))
		.andExpect(status().isUnauthorized())
		.andExpect(content().contentType("text/html;charset=UTF-8"));
		
		Mockito.reset(userService);
		Mockito.when(userService.resetPasswordTokenIsValid(Mockito.anyString())).thenReturn(0);

		mockMvc.perform(
				get("/api/v1/reset-password")
				.param("token", "mockToken"))
		.andExpect(status().isUnauthorized())
		.andExpect(content().contentType("text/html;charset=UTF-8"));
		
		Mockito.reset(userService);
		Mockito.when(userService.resetPasswordTokenIsValid(Mockito.anyString())).thenReturn(1);

		mockMvc.perform(
				get("/api/v1/reset-password")
				.param("token", "mockToken"))
		.andExpect(status().isOk())
		.andExpect(content().contentType("text/html;charset=UTF-8"));
		
	}
	
}
