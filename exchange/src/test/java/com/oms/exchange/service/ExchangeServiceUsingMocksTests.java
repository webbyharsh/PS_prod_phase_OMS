/*
package com.oms.exchange.service;

import static org.testng.Assert.assertEquals;

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
import com.oms.exchange.dto.OrderRequest;
import com.oms.exchange.dto.OrderResponse;

@SpringBootTest
@AutoConfigureMockMvc
@TestExecutionListeners(MockitoTestExecutionListener.class)
class OrderExchangeServiceUsingMocksTests extends AbstractTestNGSpringContextTests {

	@MockBean
	private IExchangeService orderService;
	@Autowired
	private MockMvc mockMvc;

	@Test
	void test() throws Exception {

		OrderResponse res = new OrderResponse(1L, "REJECTED", "Target price should be null for Market order");
		String postOrderJSON = "{\"orderId\":1,\"clientId\":102,\"quantity\": 780,\"stock\": \"Zovio\",\"side\": \"BUY\",\"type\": \"Market\",\"targetPrice\": 100}";

		Mockito.when(orderService.exchangeService(Mockito.isA(OrderRequest.class))).thenReturn(res);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/exchange/order")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(postOrderJSON);

		MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
		System.out.println(response.getContentAsString());
		OrderResponse responseToDto = new ObjectMapper().readValue(response.getContentAsString(), OrderResponse.class);

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertEquals(responseToDto.getStatus(), res.getStatus());
		assertEquals(responseToDto.getReason(), res.getReason());
		assertEquals(responseToDto.getOrderId(), res.getOrderId());
	}

}*/
