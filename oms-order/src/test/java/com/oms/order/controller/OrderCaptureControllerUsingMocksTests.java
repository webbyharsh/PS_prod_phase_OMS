package com.oms.order.controller;

import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oms.order.dto.OrderRequest;
import com.oms.order.dto.OrderResponse;
import com.oms.order.entities.Order;
import com.oms.order.exceptions.OrderNotFoundException;
import com.oms.order.service.OrderService;

@SpringBootTest
@AutoConfigureMockMvc
@TestExecutionListeners(MockitoTestExecutionListener.class)
class OrderCaptureControllerUsingMocksTests extends AbstractTestNGSpringContextTests{

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrderService orderService;
	

	// If request body is of correct format return response with status 201
	@Test
	void test_capture_order_and_status_created_if_request_ok() throws Exception {
		String postOrderJSON = "{\"clientId\":102,\"quantity\": 780,\"stock\": \"Test Stock\",\"side\": \"BUY\",\"type\": \"LIMIT\",\"targetPrice\": 100}";
		Order mockedSavedOrder = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),500L), 136L);

		Mockito.when(orderService.saveOrder(Mockito.isA(Order.class))).thenReturn(mockedSavedOrder);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/order")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("userId", "136").header("Authorization", "abc")
				.content(postOrderJSON);

		MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
		System.out.println(response.getContentAsString());
		OrderResponse responseToDto = new ObjectMapper().readValue(response.getContentAsString(), OrderResponse.class);
		
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
		assertEquals(responseToDto.getClientId().longValue(), 102L);
		assertEquals(responseToDto.getStock(), "Test Stock");

	}
	
	
	// Internal server error if unable to save data to table
	@Test
	void test_server_error_if_exception_in_service() throws Exception {
		String postOrderJSON = "{\"clientId\":102,\"quantity\": 780,\"stock\": \"Test Stock\",\"side\": \"BUY\",\"type\": \"LIMIT\",\"targetPrice\": 100}";
		Exception rEx = new RuntimeException();

		Mockito.when(orderService.saveOrder(Mockito.isA(Order.class))).thenThrow(rEx);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/order")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("userId", "136").header("Authorization", "abc")
				.content(postOrderJSON);

		MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
		assertEquals(response.getStatus(), HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
	
	// Internal server error if unable to save data to table
	@Test
	void test_status_ok_if_order_not_found_exception_in_service() throws Exception {
		String postOrderJSON = "{\"clientId\":102,\"quantity\": 780,\"stock\": \"Test Stock\",\"side\": \"BUY\",\"type\": \"LIMIT\",\"targetPrice\": 100}";

		Mockito.when(orderService.saveOrder(Mockito.isA(Order.class))).thenAnswer(invocation -> { throw new OrderNotFoundException(""); });

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/order")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("userId", "136").header("Authorization", "abc")
				.content(postOrderJSON);

		MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
		assertEquals(response.getStatus(), HttpStatus.OK.value());
	}
}
