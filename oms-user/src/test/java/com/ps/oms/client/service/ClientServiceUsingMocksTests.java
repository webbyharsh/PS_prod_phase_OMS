package com.ps.oms.client.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertEquals;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.ps.oms.client.dto.BulkUploadResponse;
import com.ps.oms.client.entities.BulkUploadData;
import com.ps.oms.client.entities.Client;
import com.ps.oms.client.repository.ClientRepository;

@SpringBootTest
@TestExecutionListeners(MockitoTestExecutionListener.class)
public class ClientServiceUsingMocksTests extends AbstractTestNGSpringContextTests {

	@Autowired
	ClientValidator clientValidator;

	@MockBean
	ClientRepository clientRepository;

	@Autowired
	ClientService clientService;

	// throw error if email already exist in database
	@Test
	void test_error_if_email_id_exist() throws Exception {

		String fileName = "sample.csv";
		MockMultipartFile file = new MockMultipartFile("file", fileName, "text/csv",
				("name, emailId, contactNumber, street, city, state, country\r\n"
						+ "testname, test@name.com, 9876543210, testStreet, testCity, testState, testCountry\r\n")
								.getBytes());

		Mockito.when(clientRepository.existsByEmailId(Mockito.anyString())).thenThrow(new RuntimeException());

		BulkUploadData bulkUploadData = clientValidator.csvFileValidation(file);

		assertThat(bulkUploadData.getEmailIdExistsErrors().size()).isPositive();

	}

	// throw error if some problem occurs during saving data into database
	@Test
	void test_failure_if_database_insertion_error() throws Exception {

		String fileName = "sample.csv";
		MockMultipartFile file = new MockMultipartFile("file", fileName, "text/csv",
				("name, emailId, contactNumber, street, city, state, country\r\n"
						+ "testname, test@name.com, 9876543210, testStreet, testCity, testState, testCountry\r\n"
						+ "testname2, test2@name.com, 9876543212, testStreet, testCity, testState, testCountry\r\n")
								.getBytes());

		Mockito.when(clientRepository.save(Mockito.isA(Client.class))).thenThrow(new RuntimeException());

		BulkUploadData bulkUploadData = clientValidator.csvFileValidation(file);
		BulkUploadResponse bulkUploadResponse = clientService.saveCsvClients(bulkUploadData, file, 1L);

		assertThat(bulkUploadResponse.getSuccessful()).isZero();
		assertEquals(bulkUploadResponse.getFailures().size(), 2);

	}

}
