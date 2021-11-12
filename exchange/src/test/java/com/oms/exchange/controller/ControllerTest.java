package com.oms.exchange.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;

import com.oms.exchange.dto.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oms.exchange.dto.OrderResponse;

@SpringBootTest
@AutoConfigureMockMvc
class ControllerTests extends AbstractTestNGSpringContextTests {

	@Autowired
	private MockMvc mockMvc;
@Autowired
ExchangeController exchangeController;
	// Will give a bad request error if no request body
	@Test
	void testStatusBadRequestIfNoRequestBody() throws Exception {
		mockMvc.perform(post("/api/v1/exchange/order").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}
	@Test
	void testStatusBadRequestIfNullRequestBody() throws Exception {
		String postOrderJSON ="{}";
		mockMvc.perform(post("/api/v1/exchange/order").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(postOrderJSON)).andExpect(status().isBadRequest());
	}

	@Test
	void testReceivedOrderResponseIfRequest() throws Exception {
		String postOrderJSON = "{\"orderId\":1,\"clientId\":102,\"quantity\": 780,\"stock\": \"Zovio\",\"side\": \"BUY\",\"type\": \"limit\",\"targetPrice\": 100}";

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/exchange/order")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(postOrderJSON);
		MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
		 Assert.assertEquals(response.getContentAsString(),"RECEIVED");

	}
	@Test
	void testExchangeOrderMethod() throws Exception {
		OrderRequest request=new OrderRequest();
		request.setOrderId(100L);
		request.setClientId(1L);
		request.setType("MARKET");
		request.setQuantity(20);
		request.setSide("BUY");
		request.setStock("Take");

	     ResponseEntity<String> message=exchangeController.mockExch(request);
	     Assert.assertEquals(message.getBody(),"RECEIVED");
	}


}
