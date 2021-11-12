package com.ps.oms.client.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;

//import java.util.List;

import org.testng.annotations.Test;

import com.ps.oms.client.entities.Address;
import com.ps.oms.client.entities.Client;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
class ClientRepositoryTests extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	private ClientRepository clientRepository;

	// test for read client to table
	@Test
	@Rollback(true)
	@Transactional
	void test_read_client() {

		Address address = new Address("Test Street", "Test City", "Test State", "Test Country");
		Client testClient = new Client("Test Name", "test@name.com", "9876543210", address, 121L);

		Client savedClient = clientRepository.save(testClient);
		Long clientId = savedClient.getClientId();
		Optional<Client> returnedClient = clientRepository.findById(clientId);

		assertNotNull(returnedClient);
	}

	// test for saving client to table
	@Test
	@Rollback(true)
	@Transactional
	void test_create_client() {
		Address address = new Address("Test Street", "Test City", "Test State", "Test Country");
		Client testClient = new Client("Test Name", "test@name.com", "9876543210", address, 121L);

		Client savedClient = clientRepository.save(testClient);
		String expectedName = "Test Name";
		String expectedEmailId = "test@name.com";
		String expectedContact = "9876543210";
		Long expectedCreatedBy = 121L;

		assertEquals(savedClient.getCreatedBy(), expectedCreatedBy);
		assertEquals(savedClient.getName(), expectedName);
		assertEquals(savedClient.getContact(), expectedContact);
		assertEquals(savedClient.getEmailId(), expectedEmailId);
		assertTrue(savedClient.getIsActive());
		assertNotNull(savedClient.getCreatedAt());
		assertNotNull(savedClient.getLastModifiedAt());
		assertNotNull(savedClient.getLastModifiedBy());

		assertThat(address).usingRecursiveComparison().isEqualTo(savedClient.getAddress());
	}

	// test for getting all clients from the table
	@Test
	@Rollback(true)
	@Transactional
	void test_list_clients() {
		Address address = new Address("Test Street", "Test City", "Test State", "Test Country");
		Client testClient = new Client("Test Name", "test@name.com", "9876543210", address, 121L);

		clientRepository.save(testClient);

		List<Client> clients = clientRepository.findAll();
		assertThat(clients).size().isPositive();
	}

	// test for deleting clients from table
	@Test
	@Rollback(true)
	@Transactional
	void test_delete_clients() {
		Address address = new Address("Test Street", "Test City", "Test State", "Test Country");
		Client testClient = new Client("Test Name", "test@name.com", "9876543210", address, 121L);

		Client savedClient = clientRepository.save(testClient);

		Long countBefore = clientRepository.count();

		clientRepository.deleteById(savedClient.getClientId());
		Long countAfter = clientRepository.count();

		assertEquals(countBefore - countAfter, 1);
	}
}
