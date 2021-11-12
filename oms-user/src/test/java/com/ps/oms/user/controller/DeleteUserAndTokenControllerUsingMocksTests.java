package com.ps.oms.user.controller;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.ps.oms.user.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@TestExecutionListeners(MockitoTestExecutionListener.class)
class DeleteUserAndTokenControllerUsingMocksTests extends AbstractTestNGSpringContextTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;
	
	@AfterMethod
	void reset_mocks() {
	    Mockito.reset(userService);
	}
	
	
	@Test
	void test_delete_user_status_accepted() throws Exception {

		doNothing().when(userService).deleteUser(Mockito.anyString());
		mockMvc.perform(post("/api/v1/delete").param("emailId", "test@mail.com")).andExpect(status().isAccepted());
	}
	

	@Test
	void test_delete_password_token_status_accepted() throws Exception {

		doNothing().when(userService).deleteUser(Mockito.anyString());
		mockMvc.perform(post("/api/v1/delete-password-token").param("emailId", "test@mail.com")).andExpect(status().isAccepted());
	}

}
