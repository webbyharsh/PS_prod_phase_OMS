package com.ps.oms.client.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.ps.oms.client.dto.BulkUploadResponse;
import com.ps.oms.client.entities.BulkUploadData;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ClientServiceTests extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	ClientValidator clientValidator;

	@Autowired
	ClientService clientService;


	// to save the data in db if csv file is valid
	@Test
	@Rollback(true)
	@Transactional
	void test_save_to_database_if_csv_valid() throws Exception {

		String fileName = "sample.csv";
		MockMultipartFile file = new MockMultipartFile("file", fileName, "text/csv",
				("name, emailId, contactNumber, street, city, state, country\r\n"
						+ "testname, test@name.com, 9876543210, testStreet, testCity, testState, testCountry\r\n"
						+ "testname, test2@name.com, 1234567890, testStreet, testCity, testState, testCountry\r\n")
								.getBytes());

		Integer countBeforeInsert = clientService.getClientCount();
		
		BulkUploadData bulkUploadData = clientValidator.csvFileValidation(file);
		BulkUploadResponse bulkUploadResponse = clientService.saveCsvClients(bulkUploadData, file, 1L);

		assertEquals(bulkUploadResponse.getTotal(), (Integer) 2);
		assertThat(bulkUploadResponse.getSuccessful()).isPositive();
		
		Integer countAfterInsert = clientService.getClientCount();
		
		assertEquals(countAfterInsert - countBeforeInsert, 2);

	}

}