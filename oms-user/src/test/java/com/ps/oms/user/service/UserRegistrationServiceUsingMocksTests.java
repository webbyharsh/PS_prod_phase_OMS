package com.ps.oms.user.service;

import static org.mockito.Mockito.doNothing;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;

import java.time.LocalDateTime;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

//import java.util.List;

import org.testng.annotations.Test;

import com.ps.oms.user.dto.UserRequest;
import com.ps.oms.user.entities.Address;
import com.ps.oms.user.entities.Role;
import com.ps.oms.user.entities.User;
import com.ps.oms.user.exceptions.UserException;
import com.ps.oms.user.kafka.IKafkaProducer;
import com.ps.oms.user.repository.RoleRepository;
import com.ps.oms.user.repository.UserRepository;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestExecutionListeners(MockitoTestExecutionListener.class)
class UserRegistrationServiceUsingMocksTests extends AbstractTestNGSpringContextTests{
	
	@Autowired
	private UserService userService;

	@MockBean
	private UserRepository userRepository;
	
	@MockBean
	private RoleRepository roleRepository;
	
	@MockBean
	private IKafkaProducer producer;
	
	//@MockBean
	//private JavaMailSender mailSender;
	
	// Test user service
	@Test
	void test_register_user_if_request_ok() throws Exception {
		Address address = new Address("Test Street", "Test City", "Test State", "Test Country");
		UserRequest testUserRequest = new UserRequest("Test Name", "test@name.com", "testNAME@123", "1234567890", address, 25);
		User testUser = new User(testUserRequest);
		
		Role testRole = new Role("User", "Authorized to use all Broker operations");
		
		Mockito.when(userRepository.existsByEmailIdIgnoreCase(Mockito.anyString())).thenReturn(false);
		Mockito.when(userRepository.save(Mockito.isA(User.class))).thenReturn(testUser);
		Mockito.when(roleRepository.findByName(Mockito.anyString())).thenReturn(testRole);
		//Mockito.when(mailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
		//doNothing().when(mailSender).send(Mockito.isA(MimeMessage.class));
		doNothing().when(producer).verificationEmailProducer(Mockito.isA(User.class), Mockito.anyString());
		
		boolean userSaved = userService.register(testUserRequest, "");
		assertTrue(userSaved);
	}
	
	
	@Test
	void test_exception_if_user_exists() throws Exception {
		Address address = new Address("Test Street", "Test City", "Test State", "Test Country");
		UserRequest testUserRequest = new UserRequest("Test Name", "test@name.com", "testNAME@123", "1234567890", address, 25);
		
		Mockito.when(userRepository.existsByEmailIdIgnoreCase(Mockito.anyString())).thenReturn(true);

		assertThrows(UserException.class, () -> userService.register(testUserRequest, ""));
	}
	
	
	@Test
	void test_verify_method_of_service() throws Exception {
		Mockito.when(userRepository.findByVerificationCode(Mockito.anyString())).thenReturn(null);

		assertEquals(userService.verify("test string"), -1);
	}
	
	
	
	@Test
	void test_when_token_expired() throws Exception {
		Address address = new Address("Test Street", "Test City", "Test State", "Test Country");
		UserRequest testUserRequest = new UserRequest("Test Name", "test@name.com", "testNAME@123", "1234567890", address, 25);
		User testUser = new User(testUserRequest);
		testUser.setCreatedAt(LocalDateTime.now().minusDays(2));
		
		Mockito.when(userRepository.findByVerificationCode(Mockito.anyString())).thenReturn(testUser);
		
		doNothing().when(userRepository).deleteByEmailId(Mockito.anyString());

		assertEquals(userService.verify("test string"), -1);
	}
	
	
	
	@Test
	void test_save_user_if_verified() throws Exception {
		Address address = new Address("Test Street", "Test City", "Test State", "Test Country");
		UserRequest testUserRequest = new UserRequest("Test Name", "test@name.com", "testNAME@123", "1234567890", address, 25);
		User testUser = new User(testUserRequest);
		testUser.setCreatedAt(LocalDateTime.now().minusHours(1));
		
		Mockito.when(userRepository.findByVerificationCode(Mockito.anyString())).thenReturn(testUser);
		Mockito.when(userRepository.save(Mockito.isA(User.class))).thenReturn(testUser);
		
		int verified = userService.verify("testCode");
		assertEquals(verified, 1);
	}
}
