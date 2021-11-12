package com.ps.oms.user.service;
import static org.mockito.Mockito.doNothing;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.ps.oms.user.dto.UserRequest;
import com.ps.oms.user.entities.Address;
import com.ps.oms.user.entities.User;
import com.ps.oms.user.repository.ResetPasswordTokenRepository;
import com.ps.oms.user.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestExecutionListeners(MockitoTestExecutionListener.class)
class DeleteUserAndTokenServiceUsingMocksTests extends AbstractTestNGSpringContextTests {

	@Autowired
	private UserService userService;

	@MockBean
	private UserRepository userRepository;
	
	@MockBean
	private ResetPasswordTokenRepository resetPasswordRepository;
	
	@AfterMethod
	void reset_mocks() {
	    Mockito.reset(userRepository);
	}
	
	
	@Test
	void test_delete_user_method() {

		doNothing().when(userRepository).deleteByEmailId(Mockito.anyString());
		userService.deleteUser("test@mail.com");
	}
	

	@Test
	void test_delete_password_token_method() {
		Address address = new Address("Test Street", "Test City", "Test State", "Test Country");
		UserRequest testUserRequest = new UserRequest("Test Name", "test@name.com", "testPASS@123", "1234567890", address, 25);
		User testUser = new User(testUserRequest);
		
		Mockito.when(userRepository.findByEmailIdIgnoreCase(Mockito.anyString())).thenReturn(testUser);
		doNothing().when(resetPasswordRepository).deleteByUser(Mockito.isA(User.class));
		
		userService.deletePasswordToken("testToken");
	}

}
