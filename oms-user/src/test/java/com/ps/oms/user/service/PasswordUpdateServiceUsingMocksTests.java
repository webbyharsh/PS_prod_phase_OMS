package com.ps.oms.user.service;

import static org.mockito.Mockito.doNothing;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

import java.time.LocalDateTime;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

//import java.util.List;

import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ps.oms.user.dto.ResetPasswordRequest;
import com.ps.oms.user.dto.UpdatePasswordRequest;
import com.ps.oms.user.dto.UserRequest;
import com.ps.oms.user.entities.Address;
import com.ps.oms.user.entities.ResetPasswordToken;
import com.ps.oms.user.entities.User;
import com.ps.oms.user.exceptions.PasswordUpdateException;
import com.ps.oms.user.exceptions.UserException;
import com.ps.oms.user.kafka.IKafkaProducer;
import com.ps.oms.user.repository.ResetPasswordTokenRepository;
import com.ps.oms.user.repository.UserRepository;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestExecutionListeners(MockitoTestExecutionListener.class)
class PasswordUpdateServiceUsingMocksTests extends AbstractTestNGSpringContextTests{
	
	@Autowired
	private UserService userService;

	@MockBean
	private UserRepository userRepository;
	
	@MockBean
	private ResetPasswordTokenRepository resetPasswordRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@MockBean
	private IKafkaProducer producer;

	
//	@AfterMethod
//	void reset_mocks() {
//	    Mockito.reset(userRepository);
//	    Mockito.reset(resetPasswordRepository);
//	    Mockito.reset(passwordEncoder);
//	}
	
	
	@Test
	void test_update_password_execution() throws PasswordUpdateException {
		UpdatePasswordRequest upReq = new UpdatePasswordRequest("testPASS@123", "TESTpass@123", "TESTpass@123");
		
		Address address = new Address("Test Street", "Test City", "Test State", "Test Country");
		UserRequest testUserRequest = new UserRequest("Test Name", "test@name.com", "testPASS@123", "1234567890", address, 25);
		User testUser = new User(testUserRequest);
		testUser.setPassword(passwordEncoder.encode("testPASS@123"));
		
		Mockito.when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(testUser);
		Mockito.when(userRepository.save(Mockito.isA(User.class))).thenReturn(testUser);
		
		userService.updatePassword(upReq, 12L);
	}
	
	@Test
	void test_update_password_throws_exception() throws PasswordUpdateException {
		UpdatePasswordRequest upReq = new UpdatePasswordRequest("testPAS@123", "TESTpass@123", "TESTpass@123");
		
		Address address = new Address("Test Street", "Test City", "Test State", "Test Country");
		UserRequest testUserRequest = new UserRequest("Test Name", "test@name.com", "testPASS@123", "1234567890", address, 25);
		User testUser = new User(testUserRequest);
		testUser.setPassword(passwordEncoder.encode("testPASS@123"));
		
		Mockito.when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(testUser);
		//Mockito.when(userRepository.save(Mockito.isA(User.class))).thenReturn(testUser);
		
		assertThrows(PasswordUpdateException.class, () -> userService.updatePassword(upReq, 12L));
		
	}
	
	@Test
	void test_reset_password_token_is_valid() {
		Address address = new Address("Test Street", "Test City", "Test State", "Test Country");
		UserRequest testUserRequest = new UserRequest("Test Name", "test@name.com", "testPASS@123", "1234567890", address, 25);
		User testUser = new User(testUserRequest);
		ResetPasswordToken rpToken = new ResetPasswordToken(testUser, "token");
		rpToken.setExpiryDate(LocalDateTime.now().plusDays(2));
		
		Mockito.when(resetPasswordRepository.findByToken(Mockito.anyString())).thenReturn(rpToken);
		
		int tokenValidity = userService.resetPasswordTokenIsValid("token");
		assertEquals(tokenValidity, 1);
	}
	
	@Test
	void test_reset_password_token_is_not_valid() {
		Address address = new Address("Test Street", "Test City", "Test State", "Test Country");
		UserRequest testUserRequest = new UserRequest("Test Name", "test@name.com", "testPASS@123", "1234567890", address, 25);
		User testUser = new User(testUserRequest);
		ResetPasswordToken rpToken = new ResetPasswordToken(testUser, "token");
		rpToken.setExpiryDate(LocalDateTime.now().minusDays(5));
		
		Mockito.when(resetPasswordRepository.findByToken(Mockito.anyString())).thenReturn(null);
		
		int tokenValidity = userService.resetPasswordTokenIsValid("token");
		assertEquals(tokenValidity, -1);
		
		Mockito.reset(resetPasswordRepository);
		Mockito.when(resetPasswordRepository.findByToken(Mockito.anyString())).thenReturn(rpToken);
		
		tokenValidity = userService.resetPasswordTokenIsValid("token");
		assertEquals(tokenValidity, 0);
		
	}
	
	@Test
	void test_reset_password_execution() throws PasswordUpdateException {
		ResetPasswordRequest rpReq = new ResetPasswordRequest("token", "TESTpass@123", "TESTpass@123");
		
		Address address = new Address("Test Street", "Test City", "Test State", "Test Country");
		UserRequest testUserRequest = new UserRequest("Test Name", "test@name.com", "testPASS@123", "1234567890", address, 25);
		User testUser = new User(testUserRequest);
		ResetPasswordToken rpToken = new ResetPasswordToken(testUser, "token");
		
		Mockito.when(resetPasswordRepository.findByToken(Mockito.anyString())).thenReturn(rpToken);
		Mockito.when(userRepository.save(Mockito.isA(User.class))).thenReturn(testUser);
		Mockito.when(resetPasswordRepository.deleteByToken(Mockito.anyString())).thenReturn(12L);
		
		userService.resetPassword(rpReq);
	}
	
	@Test
	void test_reset_password_throws_exception() {
		ResetPasswordRequest rpReq = new ResetPasswordRequest("token", "TESTword@123", "TESTpass@123");
		assertThrows(PasswordUpdateException.class, () -> userService.resetPassword(rpReq));
	}
	
	@Test
	void test_send_reset_password_email_execution() throws JsonProcessingException, UserException {
		Address address = new Address("Test Street", "Test City", "Test State", "Test Country");
		UserRequest testUserRequest = new UserRequest("Test Name", "test@name.com", "testPASS@123", "1234567890", address, 25);
		User testUser = new User(testUserRequest);
		testUser.setEnabled(true);
		ResetPasswordToken rpToken = new ResetPasswordToken(testUser, "token");
		
		Mockito.when(userRepository.findByEmailIdIgnoreCase(Mockito.anyString())).thenReturn(testUser);
		Mockito.when(resetPasswordRepository.save(Mockito.isA(ResetPasswordToken.class))).thenReturn(rpToken);
		
		doNothing().when(producer).resetPasswordEmailProducer(Mockito.isA(User.class), Mockito.anyString(), Mockito.anyString());

		userService.sendResetPasswordEmail("test@email.com", "siteURL");
	}
	
	@Test
	void test_send_reset_password_throws_exception() throws JsonProcessingException {
		Address address = new Address("Test Street", "Test City", "Test State", "Test Country");
		UserRequest testUserRequest = new UserRequest("Test Name", "test@name.com", "testPASS@123", "1234567890", address, 25);
		User testUser = new User(testUserRequest);
		testUser.setEnabled(false);
		ResetPasswordToken rpToken = new ResetPasswordToken(testUser, "token");
		
		Mockito.when(resetPasswordRepository.save(Mockito.isA(ResetPasswordToken.class))).thenReturn(rpToken);
		doNothing().when(producer).resetPasswordEmailProducer(Mockito.isA(User.class), Mockito.anyString(), Mockito.anyString());

		Mockito.when(userRepository.findByEmailIdIgnoreCase(Mockito.anyString())).thenReturn(null);
		assertThrows(UserException.class, () -> userService.sendResetPasswordEmail("test@email.com", "siteURL"));
		
		Mockito.reset(userRepository);
		Mockito.when(userRepository.findByEmailIdIgnoreCase(Mockito.anyString())).thenReturn(testUser);
		assertThrows(UserException.class, () -> userService.sendResetPasswordEmail("test@email.com", "siteURL"));
	}
	
}
