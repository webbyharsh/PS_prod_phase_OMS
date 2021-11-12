package com.ps.oms.client.service;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.validation.UnexpectedTypeException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import org.testng.annotations.Test;

@SpringBootTest
class ClientValidatorTests extends AbstractTestNGSpringContextTests {

	@Autowired
	ClientValidator clientValidator;

	// ClientValidator clientValidator = new ClientValidator();

	// to check fileformat csv or not
	@Test
	void test_return_true_if_file_csv() {
		String fileName = "sample.csv";
		MockMultipartFile file = new MockMultipartFile("file", fileName, "text/csv",
				("name, emailId, contactNumber, street, city, state, country\r\n"
						+ "testname, test@name.com, 9876543210, testStreet, testCity, testState, testCountry\r\n")
								.getBytes());

		assertTrue(clientValidator.hasCSVFormat(file));

	}

	// to check file format csv or not
	@Test
	void test_exception_if_file_not_csv() {
		String fileName = "sample.html";
		MockMultipartFile file = new MockMultipartFile("file", fileName, "text/html",
				("name, emailId, contactNumber, street, city, state, country\r\n"
						+ "testname, test@name.com, 9876543210, testStreet, testCity, testState, testCountry\r\n")
								.getBytes());

		assertThrows(UnexpectedTypeException.class, () -> clientValidator.hasCSVFormat(file));
	}

	// to check csv header correct or wrong
	@Test
	void test_return_true_if_csv_headers_correct() {
		List<String> headers = new ArrayList<>();
		headers.add("name");
		headers.add("emailId");
		headers.add("contactNumber");
		headers.add("street");
		headers.add("city");
		headers.add("state");
		headers.add("country");

		assertTrue(clientValidator.hasCorrectHeaders(headers));

	}

	// to check correct format of email
	@Test
	void test_return_true_if_email_valid() {

		assertTrue(clientValidator.isEmailAddress("test@email.com"));
		assertTrue(clientValidator.isEmailAddress("test.name@email.com"));
		assertTrue(clientValidator.isEmailAddress("test.name@email.co.in"));

	}

	// to throw error if format of email not correct
	@Test
	void test_return_false_if_email_invalid() {

		assertFalse(clientValidator.isEmailAddress("test.com"));
		assertFalse(clientValidator.isEmailAddress("test."));
		assertFalse(clientValidator.isEmailAddress("@test"));
		assertFalse(clientValidator.isEmailAddress("@test@email"));
		assertFalse(clientValidator.isEmailAddress("tes@@t.com"));

	}

	// to check correct format contact number
	@Test
	void test_return_true_if_contact_number_valid() {

		assertTrue(clientValidator.isContactNumber("1234567890"));
		assertTrue(clientValidator.isContactNumber("0123456789"));

	}

	// to throw error if format of contact number not correct
	@Test
	void test_return_false_if_contact_number_invalid() {

		assertFalse(clientValidator.isContactNumber("abc"));
		assertFalse(clientValidator.isContactNumber("12345"));
		assertFalse(clientValidator.isContactNumber("1010abcd29"));
		assertFalse(clientValidator.isContactNumber("10.20.2000"));

	}

	// to test if particular row in csv contains the right data or not
	@Test
	void test_csv_line() throws IOException {
		String fileName = "sample.csv";
		MockMultipartFile file = new MockMultipartFile("file", fileName, "text/csv",
				("name, emailId, contactNumber, street, city, state, country\r\n"
						+ "testname, test@name.com, 9876543210, testStreet, testCity, testState, testCountry\r\n"
						+ "testname ,test@name.com, , testStreet, testCity, testState, testCountry\r\n"
						+ "testname ,test@xxname.com ,9876543210, testStreet, ,testState, testCountry\r\n"
						+ "testname, test@yyname.com, 9876543210, testStreet, testCity,,testState\r\n"
						+ "testname test@yyname.com 9876543210, testStreet, testCity,teststate,,\r\n").getBytes());

		BufferedReader fileReader = new BufferedReader(
				new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));

		CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim());

		Iterator<CSVRecord> csvRecords = csvParser.iterator();

		assertTrue(clientValidator.isValidClientCsvLine(csvRecords.next()));
		assertFalse(clientValidator.isValidClientCsvLine(csvRecords.next()));
		assertFalse(clientValidator.isValidClientCsvLine(csvRecords.next()));
		assertFalse(clientValidator.isValidClientCsvLine(csvRecords.next()));

		csvParser.close();

	}

}
