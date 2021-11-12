package com.oms.fill.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testng.annotations.Test;

import com.oms.fill.dto.FillRequest;
import com.oms.fill.entities.Order;
import com.oms.fill.services.Fill;
import com.oms.fill.services.KafkaService;
import com.oms.fill.services.NotificationService;
import com.oms.fill.services.UpdateOrderService;

@SpringBootTest
@AutoConfigureMockMvc
@TestExecutionListeners(MockitoTestExecutionListener.class)
public class FillControllerTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private UpdateOrderService uds;
	@MockBean
	private NotificationService ns;

	@MockBean
	private KafkaService kafkaService;

	@Test
	void test_status_bad_request_if_no_request_body() throws Exception {
		mockMvc.perform(post("/api/v1/fill/response").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@Test
	void test_status_bad_request_if_fields_missing() throws Exception {
		String postOrderJSON = "{\"orderId\":188,\"exchangeId\":102,\"quantity\": 780,\"stock\": \"Zovio\"}";

		mockMvc.perform(post("/api/v1/fill/response").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(postOrderJSON)).andExpect(status().isBadRequest());

	}

	@Test
	void test_accepeted_response_if_request_is_correct() throws Exception {
		String postOrderJSON = "{\"orderId\":188,\"exchangeId\":1,\"quantity\": 780,\"stock\": \"Zovio\",\"executedPrice\": 100,\"executedTime\":\"2021-01-25T21:34:55\"}";

		Order testOrder = new Order();
		testOrder.setOrderId(1L);
		testOrder.setClientId(1L);
		testOrder.setCreatedBy(2L);
		testOrder.setQuantity(200);

		Mockito.when(uds.updateOrder(Mockito.isA(FillRequest.class))).thenReturn(testOrder);
		Mockito.when(ns.sendNotificationToBroker(Mockito.isA(Order.class), Mockito.isA(Fill.class))).thenReturn(true);
		Mockito.when(ns.sendNotificationToClient(Mockito.isA(Order.class), Mockito.isA(Fill.class))).thenReturn(true);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/fill/response")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(postOrderJSON);
		mockMvc.perform(requestBuilder).andExpect(status().isOk());

	}

}
