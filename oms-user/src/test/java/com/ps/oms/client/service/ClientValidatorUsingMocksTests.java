package com.ps.oms.client.service;

import static org.junit.Assert.assertFalse;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;

import java.io.IOException;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.ps.oms.client.entities.BulkUploadData;
import com.ps.oms.client.exceptions.IncorrectCsvHeaderException;

@SpringBootTest
@TestExecutionListeners(MockitoTestExecutionListener.class)
public class ClientValidatorUsingMocksTests extends AbstractTestNGSpringContextTests {

	@Autowired
	ClientValidator clientValidator;

	@MockBean
	ClientService clientService;

	// test file validation
	@Test
	void test_csv_file_validation_if_data_valid() throws IOException, IncorrectCsvHeaderException {
		String fileName = "sample.csv";
		MockMultipartFile file = new MockMultipartFile("file", fileName, "text/csv",
				("name, emailId, contactNumber, street, city, state, country\r\n"
						+ "testname, test@name.com, 9876543210, testStreet, testCity, testState, testCountry\r\n"
						+ "testname, test2@name.com, 1234567890, testStreet, testCity, testState, testCountry\r\n")
								.getBytes());

		Mockito.when(clientService.emailAlreadyExists(Mockito.anyString())).thenReturn(false);

		BulkUploadData bulkUploadData = clientValidator.csvFileValidation(file);

		assertTrue(bulkUploadData.isValidCSV());
		assertEquals(bulkUploadData.getTotal(), (Integer) 2);
		assertEquals(bulkUploadData.getValidRows().size(), 2);

	}

	@Test
	void test_csv_file_validation_if_data_invalid() throws IOException, IncorrectCsvHeaderException {
		String fileName = "sample.csv";
		MockMultipartFile file = new MockMultipartFile("file", fileName, "text/csv",
				("name, emailId, contactNumber, street, city, state, country\r\n"
						+ "testname test@name.com 9876543210, testStreet, testCity, testState, testCountry\r\n"
						+ "testname, test, 9876543210, testStreet, testCity, testState, testCountry\r\n"
						+ "testname, testname.com, 9876543210, testStreet, testCity, testState, testCountry\r\n"
						+ "testname, test2@name.com, 1234567890, testStreet, testCity, testState, testCountry\r\n"
						+ "testname, test2@name.com, 1234_error, testStreet, testCity, testState, testCountry\r\n")
								.getBytes());

		Mockito.when(clientService.emailAlreadyExists(Mockito.anyString())).thenReturn(true);

		BulkUploadData bulkUploadData = clientValidator.csvFileValidation(file);

		assertFalse(bulkUploadData.isValidCSV());
		assertEquals(bulkUploadData.getTotal(), (Integer) 5);
		assertEquals(bulkUploadData.getEmailIdExistsErrors().size(), 2);
		assertEquals(bulkUploadData.getContactNumberErrors().size(), 1);
		assertEquals(bulkUploadData.getEmailIdErrors().size(), 2);
		assertEquals(bulkUploadData.getCsvDuplicateEmailIdErrors().size(), 1);
		assertEquals(bulkUploadData.getValidRows().size(), 0);
		assertEquals(bulkUploadData.getCsvErrors().size(), 1);
	}

	// test if csv file header is invalid
	@Test
	void test_csv_file_validation_if_csv_header_invalid() throws IOException {
		String fileName = "sample.csv";
		MockMultipartFile file = new MockMultipartFile("file", fileName, "text/csv",
				("name emailId, contactNumber, street, city, state, country\r\n"
						+ "testname test@name.com 9876543210, testStreet, testCity, testState, testCountry\r\n")
								.getBytes());

		Mockito.when(clientService.emailAlreadyExists(Mockito.anyString())).thenReturn(true);

		assertThrows(IncorrectCsvHeaderException.class, () -> clientValidator.csvFileValidation(file));
	}

}
