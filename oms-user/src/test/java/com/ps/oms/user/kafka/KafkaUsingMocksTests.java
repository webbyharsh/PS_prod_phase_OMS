package com.ps.oms.user.kafka;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.util.concurrent.SettableListenableFuture;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ps.oms.user.dto.UserRequest;
import com.ps.oms.user.dto.UserUpdateRequest;
import com.ps.oms.user.entities.Address;
import com.ps.oms.user.entities.User;

@Ignore("Unable to mock kafkaTemplate, will actually send data to kafka")
@SpringBootTest
@TestExecutionListeners(MockitoTestExecutionListener.class)
public class KafkaUsingMocksTests  extends AbstractTestNGSpringContextTests {
	
	@Autowired
	private IKafkaProducer producer;
	
	@MockBean
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Test
	void test_producer_send_message() throws JsonProcessingException {
		Address address = new Address("Test Street", "Test City", "Test State", "Test Country");
		UserRequest testUserRequest = new UserRequest("Test Name", "test@name.com", "testNAME@123", "1234567890", address, 25);
		User testUser = new User(testUserRequest);
		
		SettableListenableFuture<SendResult<String, String>> future = new SettableListenableFuture<>();
		Mockito.when(kafkaTemplate.send(Mockito.anyString(), Mockito.anyString())).thenReturn(future);
		producer.verificationEmailProducer(testUser, "mock.site");
		
	}
	
	
	@Test
	void test_producer_update_profile_change_notification() throws JsonProcessingException {
		Address address = new Address("Test Street", "Test City", "Test State", "Test Country");
		Integer intage = Integer.valueOf(22);
		Long id1= Long.valueOf(1);
		UserUpdateRequest testUserUpdateRequest = new UserUpdateRequest(id1,"Test Name", address, "1234567890","test@name.com", intage);
	
		
		SettableListenableFuture<SendResult<String, String>> future = new SettableListenableFuture<>();
		Mockito.when(kafkaTemplate.send(Mockito.anyString(), Mockito.anyString())).thenReturn(future);
		producer.updateProfileChangeNotification(testUserUpdateRequest);
		
	}

}
