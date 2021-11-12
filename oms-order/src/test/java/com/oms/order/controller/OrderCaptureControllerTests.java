package com.oms.order.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oms.order.dto.ErrorResponse;


@SpringBootTest
@AutoConfigureMockMvc
class OrderCaptureControllerTests extends AbstractTestNGSpringContextTests{

	@Autowired
	private MockMvc mockMvc;

	
	// Will give a bad request error if no request body
	@Test
	void test_status_bad_request_if_no_request_body() throws Exception {
		mockMvc.perform(
				post("/api/v1/order")
				.header("userId", "146").header("Authorization", "abc"))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message", is("Malformed JSON request")));
	}
	
	// Will give a bad request error if request body is not json
	@Test
	void test_status_bad_request_if_request_is_string() throws Exception {
		String postOrderString = "{clientId:102, quantity: 780, stock: Test Stock, side: BUY, type: MARKET, targetPrice: 100}";
		mockMvc.perform(
				post("/api/v1/order")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("userId", "136").header("Authorization", "abc")
				.content(postOrderString))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message", is("Malformed JSON request")));
	}
	
	
	// Will give a bad request error if json is not of correct format
	// i.e if datatype of field is incorrect and casting is not possible
	// fields are not present or null
	@Test
	void test_status_bad_request_if_fields_missing() throws Exception {
		String postOrderJSON = "{\"clientId\":2,\"quantity\": 10}";
		
		mockMvc.perform(
				post("/api/v1/order")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("userId", "136").header("Authorization", "abc")
				.content(postOrderJSON))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message", is("One or more fields are not present")));

	}
	
	// Bad Request if validation fails
	@Test
	void test_status_bad_request_if_field_value_not_correct() throws Exception {
		String postOrderJSON = "{\"clientId\":102,\"quantity\": 780,\"stock\": \"Test Stock\",\"side\": \"BUY\",\"type\": \"ABC\",\"targetPrice\": 100}";
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/order")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("userId", "136")
				.header("Authorization", "abc")
				.content(postOrderJSON);

		MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();

		ErrorResponse responseToDto = new ObjectMapper().readValue(response.getContentAsString(), ErrorResponse.class);
		
		assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
		assertEquals(responseToDto.getMessage(), "Type must be MARKET or LIMIT");

	}
	
	// Will give bad_request if no header
	@Test
	void test_bad_request_if_userid_not_in_header() throws Exception {
		String postOrderJSON = "{\"clientId\":102,\"quantity\": 780,\"stock\": \"Test Stock\",\"side\": \"BUY\",\"type\": \"LIMIT\",\"targetPrice\": 100}";
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/order")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "abc")
				.content(postOrderJSON);

		MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
		
		ErrorResponse responseToDto = new ObjectMapper().readValue(response.getContentAsString(), ErrorResponse.class);
		
		assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
		assertEquals(responseToDto.getMessage(), "Required request header 'userId' for method parameter type Long is not present");
	}
	
	// Will give bad request if header incorrect
	@Test
	void test_bad_request_if_header_incorrect() throws Exception {
		String postOrderJSON = "{\"clientId\":102,\"quantity\": 780,\"stock\": \"Test Stock\",\"side\": \"BUY\",\"type\": \"LIMIT\",\"targetPrice\": 100}";
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/order")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("userId", "abc")
				.header("Authorization", "abc")
				.content(postOrderJSON);

		MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
		
		ErrorResponse responseToDto = new ObjectMapper().readValue(response.getContentAsString(), ErrorResponse.class);
		
		assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
		assertEquals(responseToDto.getMessage(), "Malformed Header");
	}
		
}
