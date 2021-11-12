package com.ps.oms.user.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ps.oms.user.dto.UserDetailResponse;
import com.ps.oms.user.dto.UserUpdateRequest;
import com.ps.oms.user.entities.Address;
import com.ps.oms.user.entities.User;
import com.ps.oms.user.exceptions.ResourceNotFoundException;
import com.ps.oms.user.exceptions.UpdateNotAllowedException;
import com.ps.oms.user.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class TestController_3 extends AbstractTestNGSpringContextTests {

	@Autowired
	private UserController userController;
	@MockBean
	@Autowired
	private UserService userService;
	@Autowired
	private MockMvc mockMvc;

	LocalDateTime dateTime = null;
	final String str = "2021-07-29T17:01:53.405373";

	@Test(enabled = false)
	public void getUserById() throws ResourceNotFoundException {
		when(this.userService.getUserDetails(1L)).thenReturn(userHavingIdOne());
		ResponseEntity<UserDetailResponse> entity = userController.userDetailsView(1L, "1");
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		UserDetailResponse userResponse = new UserDetailResponse(userHavingIdOne());
		assertEquals(entity.getBody(), userResponse);
	}

	@Test(enabled = false)
	public void userDetailsViewUnauthorized() throws Exception {
		doNothing().when(this.userService).updateUser(updateUserHavingIdOne(), 1L);
		this.mockMvc.perform(get("/api/v1/userDetails/").param("user_id", "1").header("Authorization", "")).andDo(print())
				.andExpect(status().isUnauthorized()).andDo(print());
	}

	@Test(enabled = false)
	public void userDetailsViewResourceNotFoundException() throws Exception {
		doThrow(new ResourceNotFoundException("User", "Id", 1)).when(userService).getUserDetails(1L);
		this.mockMvc.perform(get("/api/v1/userDetails/").param("user_id", "1").header("Authorization", "1")).andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(content().string("\"User not found with Id: '1'\"")).andDo(print());
	}

	@Test(enabled = false)
	public void updateUserSuccess() throws Exception {
		ObjectMapper map = new ObjectMapper();
		String jsonStr = map.writeValueAsString(updateUserHavingIdOne());
		doNothing().when(this.userService).updateUser(updateUserHavingIdOne(), 1L);
		this.mockMvc
				.perform(put("/api/v1/updateDetails/" + "1").header("Authorization", "1")
						.contentType(MediaType.APPLICATION_JSON).content(jsonStr))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.responseBody").value("Success"))
				.andDo(print());
	}

	@Test(enabled = false)
	public void updateUserUnauthorized() throws Exception {
		ObjectMapper map = new ObjectMapper();
		String jsonStr = map.writeValueAsString(updateUserHavingIdOne());
		doNothing().when(this.userService).updateUser(updateUserHavingIdOne(), 1L);
		this.mockMvc
				.perform(put("/api/v1/user-profile/" + "1").header("Authorization", "")
						.contentType(MediaType.APPLICATION_JSON).content(jsonStr))
				.andDo(print()).andExpect(status().isUnauthorized()).andDo(print());
	}

	@Test(enabled = false)
	public void updateUserUpdateNotAllowedException() throws Exception {
		ObjectMapper map = new ObjectMapper();
		String jsonStr = map.writeValueAsString(updateUserHavingIdOne());
		doThrow(new UpdateNotAllowedException("Email", "User", "Id", 1)).when(userService)
				.updateUser(updateUserHavingIdOne(), 1L);
		this.mockMvc
				.perform(put("/api/v1/user-profile/" + "1").header("Authorization", "1")
						.contentType(MediaType.APPLICATION_JSON).content(jsonStr))
				.andDo(print()).andExpect(status().isForbidden())
				.andExpect(jsonPath("$.responseBody").value("Email update not allowed on User with Id: '1'"))
				.andDo(print());
	}

	@Test(enabled = false)
	public void updateUserResourceNotFoundException() throws Exception {
		ObjectMapper map = new ObjectMapper();
		String jsonStr = map.writeValueAsString(updateUserHavingIdOne());
		doThrow(new ResourceNotFoundException("User", "Id", 1)).when(userService).updateUser(updateUserHavingIdOne(),
				1L);
		this.mockMvc
				.perform(put("/api/v1/user-profile/" + "1").header("Authorization", "1")
						.contentType(MediaType.APPLICATION_JSON).content(jsonStr))
				.andDo(print()).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.responseBody").value("User not found with id: '1'")).andDo(print());
	}

	@AfterMethod
	public void afterMethod() {
		Mockito.reset(userService);
	}

	@Test(enabled = false)
	public User userHavingIdOne() {
		User expectedUser = new User();
		expectedUser.setName("sounak");
		expectedUser.setUserId(1L);
		expectedUser.setAge(22);
		expectedUser.setActive(true);
		expectedUser.setContact("2553678");
		expectedUser.setEmailId("sounak@gmail.com");
		expectedUser.setPassword("XXXX");
		expectedUser.setCreatedAt(dateTime);
		expectedUser.setLastActiveAt(dateTime);
		return expectedUser;
	}

	@Test(enabled = false)
	public UserUpdateRequest updateUserHavingIdOne() {
		UserUpdateRequest updateRequestBody = new UserUpdateRequest();
		updateRequestBody.setName("snk");
		updateRequestBody.setAge(21);
		updateRequestBody.setContact("9434555667");
		updateRequestBody.setEmailId("sounak@gmail.com");
		updateRequestBody.setAddress(new Address("234", "b", "bangalore","india"));
		updateRequestBody.setUserId(1L);
		return updateRequestBody;
	}

	@BeforeClass
	public void setup() {
		dateTime = LocalDateTime.parse(str, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

	}
}
