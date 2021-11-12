package com.ps.oms.client.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

import com.ps.oms.client.repository.ClientRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientGetClientsControllerTests extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ClientRepository clientRepository;

	@Test
	@Rollback(true)
	@Transactional
	public void test_status_created_if_csv_valid() throws Exception {

		String fileName = "sample.csv";
		MockMultipartFile file = new MockMultipartFile("file", fileName, "text/csv",
				("name, emailId, contactNumber, street, city, state, country\r\n"
						+ "testname, test@name.com, 9876543210, testStreet, testCity, testState, testCountry\r\n"
						+ "testname, test2@name.com, 9876543210, testStreet, testCity, testState, testCountry\r\n")
								.getBytes());

		MockMultipartHttpServletRequestBuilder multipartRequest = MockMvcRequestBuilders
				.multipart("/api/v1/client/upload-csv");

		mockMvc.perform(multipartRequest.file(file).header("userId", 121)).andExpect(status().isCreated());
		
		mockMvc.perform(get("/api/v1/client").param("keyword", "test")).andExpect(status().isOk());
		
		mockMvc.perform(get("/api/v1/client").param("keyword", "test3")).andExpect(status().isNotFound());
	}

}
