package com.ps.oms.client.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.ps.oms.client.entities.Address;
import com.ps.oms.client.entities.Client;
import com.ps.oms.client.repository.ClientRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientUploadCsvControllerTests extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ClientRepository clientRepository;

	// will give unsupportedMediaType error if file is not of csv format
	@Test
	public void test_unsupported_media_type_if_file_not_csv() throws Exception {

		String fileName = "sample.html";
		MockMultipartFile file = new MockMultipartFile("file", fileName, "text/html",
				("name, emailId, contactNumber, street, city, state, country\r\n"
						+ "testname, test@name.com, 9876543210, testStreet, testCity, testState, testCountry\r\n")
								.getBytes());

		MockMultipartHttpServletRequestBuilder multipartRequest = MockMvcRequestBuilders
				.multipart("/api/v1/client/upload-csv");

		mockMvc.perform(multipartRequest.file(file).header("userId", 12)).andExpect(status().isUnsupportedMediaType());

	}

	// will give badrequest if header containing userid not passed in request
	@Test
	public void test_bad_Request_if_no_header() throws Exception {

		String fileName = "sample.csv";
		MockMultipartFile file = new MockMultipartFile("file", fileName, "text/csv",
				("name, emailId, contactNumber, street, city, state, country\r\n"
						+ "testname, test@name.com, 9876543210, testStreet, testCity, testState, testCountry\r\n")
								.getBytes());

		MockMultipartHttpServletRequestBuilder multipartRequest = MockMvcRequestBuilders
				.multipart("/api/v1/client/upload-csv");

		mockMvc.perform(multipartRequest.file(file)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.message",
				is("Required request header 'userId' for method parameter type Long is not present")));

	}

	// will give badrequest if userid present in header is not of long type
	@Test
	public void test_bad_Request_if_bad_header() throws Exception {

		String fileName = "sample.csv";
		MockMultipartFile file = new MockMultipartFile("file", fileName, "text/csv",
				("name, emailId, contactNumber, street, city, state, country\r\n"
						+ "testname, test@name.com, 9876543210, testStreet, testCity, testState, testCountry\r\n")
								.getBytes());

		MockMultipartHttpServletRequestBuilder multipartRequest = MockMvcRequestBuilders
				.multipart("/api/v1/client/upload-csv");

		mockMvc.perform(multipartRequest.file(file).header("userId", "abc")).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("Malformed Header")));

	}

	// return badrequest error if request part 'file' is not present
	@Test
	public void test_exception_if_file_not_present() throws Exception {

		String fileName = "sample.txt";
		MockMultipartFile file = new MockMultipartFile("temp", fileName, "text/csv",
				("name, emailId, contactNumber, street, city, state, country\r\n"
						+ "testname, test@name.com, 9876543210, testStreet, testCity, testState, testCountry\r\n")
								.getBytes());

		MockMultipartHttpServletRequestBuilder multipartRequest = MockMvcRequestBuilders
				.multipart("/api/v1/client/upload-csv");

		mockMvc.perform(multipartRequest.file(file).header("userId", 12)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("Required request part 'file' is not present")));

	}

	// return badrequest if file is not multipart
	@Test
	public void test_exception_if_request_not_multipart() throws Exception {

		mockMvc.perform(post("/api/v1/client/upload-csv").header("userId", 12)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("Current request is not a multipart request")));

	}

	// return badrequest if email in csv file is duplicate
	@Test
	public void test_bad_Request_if_email_duplicate() throws Exception {

		String fileName = "sample.csv";
		MockMultipartFile file = new MockMultipartFile("file", fileName, "text/csv",
				("name, emailId, contactNumber, street, city, state, country\r\n"
						+ "testname, test@name.com, 9876543210, testStreet, testCity, testState, testCountry\r\n"
						+ "testname2, test@name.com, 9856543210, testStreet2, testCity2, testState2, testCountry2\r\n")
								.getBytes());

		MockMultipartHttpServletRequestBuilder multipartRequest = MockMvcRequestBuilders
				.multipart("/api/v1/client/upload-csv");

		mockMvc.perform(multipartRequest.file(file).header("userId", 12)).andExpect(status().isUnprocessableEntity());

	}

	// return error if CSV Headers Do not match required format
	@Test
	public void test_incorrect_headers_in_CSV() throws Exception {

		String fileName = "sample.csv";
		MockMultipartFile file = new MockMultipartFile("file", fileName, "text/csv",
				("name, emailId, contactNumber, street, state,city, country\r\n"
						+ "testname, test@name.com, 9876543210, testStreet, testCity, testState, testCountry\r\n")
								.getBytes());

		MockMultipartHttpServletRequestBuilder multipartRequest = MockMvcRequestBuilders
				.multipart("/api/v1/client/upload-csv");

		mockMvc.perform(multipartRequest.file(file).header("userId", 12)).andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.message", is("File not CSV or CSV Headers Do not match required format")));

	}

	// return success if database return clients successfully
	@Test
	@Rollback(true)
	@Transactional
	public void test_client_count() throws Exception {

		Address address = new Address("Test Street", "Test City", "Test State", "Test Country");
		Client testClient = new Client("Test Name", "test@name.com", "9876543210", address, 121L);
		clientRepository.save(testClient);
		
		MockHttpServletRequestBuilder mockGetRequest = MockMvcRequestBuilders.get("/api/v1/client-count");

		mockMvc.perform(mockGetRequest).andExpect(status().is2xxSuccessful());

	}
	
	
	
	// will give status created if csv valid
	@Test
	@Rollback(true)
	@Transactional
	public void test_status_created_if_csv_valid() throws Exception {

		String fileName = "sample.csv";
		MockMultipartFile file = new MockMultipartFile("file", fileName, "text/csv",
				("name, emailId, contactNumber, street, city, state, country\r\n"
						+ "testname, test@name.com, 9876543210, testStreet, testCity, testState, testCountry\r\n")
								.getBytes());

		MockMultipartHttpServletRequestBuilder multipartRequest = MockMvcRequestBuilders
				.multipart("/api/v1/client/upload-csv");

		mockMvc.perform(multipartRequest.file(file).header("userId", 121)).andExpect(status().isCreated());

	}

}
