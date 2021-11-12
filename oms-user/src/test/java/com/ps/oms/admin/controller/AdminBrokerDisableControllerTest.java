package com.ps.oms.admin.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.ps.oms.admin.dto.BrokerStatusServiceRequest;
import com.ps.oms.admin.dto.BrokerStatusServiceResponse;
import com.ps.oms.admin.entities.AdminBrokerDisableUser;
import com.ps.oms.admin.exception.ResourceNotFoundException;
import com.ps.oms.admin.exception.UpdateNotAllowedException;
import com.ps.oms.admin.service.AdminBrokerDisableUserService;

@SpringBootTest
public class AdminBrokerDisableControllerTest extends AbstractTestNGSpringContextTests {
  
	@Autowired
	private AdminBrokerDisableUserController adminBrokerDisableUserController;
	@MockBean
	@Autowired
	private AdminBrokerDisableUserService adminBrokerDisableUserService;
	
	@MockBean
	private BrokerStatusServiceResponse brokerStatusServiceResponse;
	

	BrokerStatusServiceRequest brokerRequest=new BrokerStatusServiceRequest();
	@Test
	public void getAllBrokerList() throws ResourceNotFoundException {
		
		when(this.adminBrokerDisableUserService.getAllBroker()).thenReturn(brokerList());
		ResponseEntity<List<AdminBrokerDisableUser>> entity = adminBrokerDisableUserController.getAllBrokerList("JWTtoken");
		assertEquals(entity.getStatusCode(), HttpStatus.OK);
		assertEquals(entity.getBody().size(), 2);
	}
	
	@Test
	public void getAllBrokerList2() throws ResourceNotFoundException {
		
		List<AdminBrokerDisableUser> brokers = new ArrayList<AdminBrokerDisableUser>();
		when(this.adminBrokerDisableUserService.getAllBroker()).thenReturn(brokers);
		ResponseEntity<List<AdminBrokerDisableUser>> entity = adminBrokerDisableUserController.getAllBrokerList("JWTtoken");
		assertEquals(entity.getStatusCode(), HttpStatus.NOT_FOUND);
		assertEquals(entity.getBody().size(), 0);
	}
	
	@Test
	public void updateStatus_200() throws Exception {
		brokerRequest.setUserId("1");
		brokerRequest.setIsActive("true");
		ResponseEntity<String> entity = adminBrokerDisableUserController.updateStatus(brokerRequest, "1");
		assertEquals(entity.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	public void updateStatus_403() throws Exception {
		doThrow(new UpdateNotAllowedException(1, true)).when(adminBrokerDisableUserService)
		.setStatusUpdate(1, true);
		brokerRequest.setUserId("1");
		brokerRequest.setIsActive("true");
		ResponseEntity<String> entity = adminBrokerDisableUserController.updateStatus(brokerRequest, "1");
		assertEquals(entity.getStatusCode(), HttpStatus.FORBIDDEN);
	}
	
	@Test
	public void updateStatus_404() throws Exception {
		doThrow(new ResourceNotFoundException("User", "Id", 1)).when(adminBrokerDisableUserService)
		.setStatusUpdate(1, true);
		brokerRequest.setUserId("1");
		brokerRequest.setIsActive("true");
		ResponseEntity<String> entity = adminBrokerDisableUserController.updateStatus(brokerRequest, "1");
		assertEquals(entity.getStatusCode(), HttpStatus.NOT_FOUND);
	}
	
	@Test
	public void updateStatus_401() throws Exception {
		brokerRequest.setUserId("1");
		brokerRequest.setIsActive("true");
		ResponseEntity<String> entity = adminBrokerDisableUserController.updateStatus(brokerRequest, "");
		assertEquals(entity.getStatusCode(), HttpStatus.UNAUTHORIZED);
	}
	
	@Test
	public void updateStatus_400() throws Exception {
		brokerRequest.setUserId("1");
		brokerRequest.setIsActive("");
		ResponseEntity<String> entity = adminBrokerDisableUserController.updateStatus(brokerRequest, "1");
		assertEquals(entity.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	public List<AdminBrokerDisableUser> brokerList() {
		List<AdminBrokerDisableUser> brokers = new ArrayList<AdminBrokerDisableUser>();
		AdminBrokerDisableUser broker1 = new AdminBrokerDisableUser();
		broker1.setUserId(1);
		broker1.setName("Riya");
		broker1.setActive(false);
		AdminBrokerDisableUser broker2 = new AdminBrokerDisableUser();
		broker2.setUserId(2);
		broker2.setName("Sounak");
		broker2.setActive(true);
		brokers.add(broker1);
		brokers.add(broker2);
		return brokers;
	}
	
	@AfterClass
	public void afterMethod() {
		Mockito.reset(adminBrokerDisableUserService);
	}
}
