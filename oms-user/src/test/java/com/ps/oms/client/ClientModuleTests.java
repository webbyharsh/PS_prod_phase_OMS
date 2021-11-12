package com.ps.oms.client;

import static org.testng.Assert.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.ps.oms.client.controller.ClientController;
import com.ps.oms.client.repository.ClientRepository;
import com.ps.oms.client.service.ClientService;

@SpringBootTest
class ClientModuleTests extends AbstractTestNGSpringContextTests{

	@Autowired
	private ClientController clientController;
	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private ClientService clientService;

	@Test
	void client_controller_loaded() {
		assertNotNull(clientController);
	}

	@Test
	void client_repository_loaded() {
		assertNotNull(clientRepository);
	}

	@Test
	void client_service_loaded() {
		assertNotNull(clientService);
	}
}
