package com.ps.oms.admin.service;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ps.oms.admin.entities.AdminBrokerDisableUser;
import com.ps.oms.admin.exception.ResourceNotFoundException;
import com.ps.oms.admin.exception.UpdateNotAllowedException;
import com.ps.oms.admin.repository.AdminBrokerDisableUserRepository;

@SpringBootTest
@TestExecutionListeners(MockitoTestExecutionListener.class)
public class AdminBrokerDisableServicesTest extends AbstractTestNGSpringContextTests{
  
	@Autowired
	private IAdminBrokerDisableUserService adminBrokerDisableUserService;
	@MockBean
	private AdminBrokerDisableUserRepository adminBrokerDisableUserRepository;
	
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
	
	@Test
	public void getAllBroker() throws ResourceNotFoundException {
		when(adminBrokerDisableUserRepository.findAll()).thenReturn(brokerList());
		List<AdminBrokerDisableUser> brokers = adminBrokerDisableUserService.getAllBroker();
		Assert.assertEquals(brokers.get(0).getUserId(), brokerList().get(0).getUserId());
		Assert.assertEquals(brokers.get(0).getName(), brokerList().get(0).getName());
		Assert.assertEquals(brokers.get(0).isActive(), brokerList().get(0).isActive());
	}
	
	@Test
	public void setStatusUpdate() throws ResourceNotFoundException, UpdateNotAllowedException {
		when(adminBrokerDisableUserRepository.findById(1)).thenReturn(Optional.of(brokerWithIdOne(1, "Riya", true)));
		try {
			adminBrokerDisableUserService.setStatusUpdate(1, true);
		}
		catch (UpdateNotAllowedException e) {
			Assert.assertEquals(e.getMessage(), "Update not allowed on status with UserId 1 for status  true");
		}
	}
	
	@Test
	public void setStatusUpdate2() throws ResourceNotFoundException, UpdateNotAllowedException {
		when(adminBrokerDisableUserRepository.findById(1)).thenReturn(Optional.of(brokerWithIdOne(1, "Riya", true)));
		try {
			adminBrokerDisableUserService.setStatusUpdate(1, false);
		}
		catch (UpdateNotAllowedException e) {
			Assert.assertEquals(e.getMessage(), "Update not allowed on status with UserId 1 for status  true");
		}
	}
	
	@Test
	public void setStatusUpdate3() throws ResourceNotFoundException, UpdateNotAllowedException {
		when(adminBrokerDisableUserRepository.findById(1)).thenReturn(Optional.of(brokerWithIdOne(1, "Riya", true)));
		try {
			adminBrokerDisableUserService.setStatusUpdate(2, true);
		}
		catch (ResourceNotFoundException e) {
			Assert.assertEquals(e.getMessage(), "User not found with id: '2'");
		}
	}
	
	public AdminBrokerDisableUser brokerWithIdOne(int userId, String name, boolean active) {
		AdminBrokerDisableUser broker = new AdminBrokerDisableUser();
		broker.setUserId(userId);
		broker.setName(name);
		broker.setActive(active);
		return broker;
	}
}
