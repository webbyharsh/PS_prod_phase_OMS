package com.oms.order.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import com.oms.order.dto.ErrorResponse;
import com.oms.order.dto.OrderRequest;
import com.oms.order.dto.OrderListResponse;
import com.oms.order.entities.Order;
import com.oms.order.exceptions.BadRequestException;
import com.oms.order.repository.OrderRepository;

//--------Requires Server to be in running state---------

@SpringBootTest
public class OrderListControllerTests extends AbstractTransactionalTestNGSpringContextTests {

	private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderController orderController;

	@Autowired
	private OrderControllerExceptionHandler orderControllerExceptionHandler;

	private List<Order> expectedList = new ArrayList<Order>();

	private List<Order> actualList = new ArrayList<Order>();

	private String baseUrl = "http://localhost:9003/api/v1/order/";

	@BeforeClass
	public void setUp() {
		Order order1 = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),500L), 136L);
		order1.setClientId(123L);
		order1.setCreatedBy(10_000_000L);
		order1.setStock("SECOND");
		Order addedOrder1 = orderRepository.save(order1);
		expectedList.add(addedOrder1);
		actualList.add(addedOrder1);

		Order order2 = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),500L), 136L);
		order2.setCreatedBy(10_000_000L);
		order2.setStock("FIRST");
		Order addedOrder2 = orderRepository.save(order2);
		expectedList.add(addedOrder2);
		actualList.add(addedOrder2);

		Order order3 = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),500L), 136L);
		order3.setCreatedBy(10_000_001L);
		order3.setStock("ITC");
		Order addedOrder3 = orderRepository.save(order3);
		Collections.reverse(expectedList);
		actualList.add(addedOrder3);


	}

	@AfterClass
	public void clean()
	{
		for(Order o:actualList)
		{
			orderRepository.delete(o);
		}

	}

	// Test for checking if returned response is correct
	@Ignore
	@Test
	public void testIfReturnResponseIsCorrect() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("userId", "1");
		HttpEntity<String> httpEntity = new HttpEntity<>("Header", httpHeaders);
		URI targetUrl = UriComponentsBuilder.fromUriString(baseUrl).build().encode().toUri();
		ResponseEntity<OrderListResponse> response = restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, OrderListResponse.class);
		OrderListResponse olr = response.getBody();
		List<Order> listOrder = olr.getOrders();
		Assert.assertTrue(listOrder.size() > 0);

	}

	// Test for checking if returned response is throwing exceptions
	@Ignore
	@Test
	public void testIfReturnResponseThrowsException() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("userId", "abc");
		try{
			HttpEntity<String> httpEntity = new HttpEntity<>("Header", httpHeaders);
			URI targetUrl = UriComponentsBuilder.fromUriString(baseUrl).build().encode().toUri();
			ResponseEntity<ErrorResponse> response = restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, ErrorResponse.class);
		} catch (HttpClientErrorException ex){
			Assert.assertTrue(true);
		}

	}

	// Testing contoller function if their is a response
	@Test
	public void testControllerFunctionIfResponse() throws Exception {
		ResponseEntity<OrderListResponse> response = orderController.getOrderListByBrokerId("10000000", 0, 5, null);
		OrderListResponse olr = response.getBody();
		List<Order> listOrder = olr.getOrders();
		Assert.assertTrue(listOrder.size() > 0);
	}

	// Testing controller function if their is no response
	@Test
	public void testControllerFunctionIfNoResponse() throws Exception {
		ResponseEntity<OrderListResponse> response = orderController.getOrderListByBrokerId("20000000", 0, 5, null);
		OrderListResponse olr = response.getBody();
		List<Order> listOrder = olr.getOrders();
		Assert.assertEquals(listOrder.size(), 0);
	}

	// Test for checking controller function if their is a bad request exception
	@Test
	public void testControllerFunctionIfBadRequestException(){
		try{
			ResponseEntity<OrderListResponse> response = orderController.getOrderListByBrokerId("a", 0, 5, null);
		} catch (BadRequestException ex){
			orderControllerExceptionHandler.handleException(ex);
			Assert.assertEquals(ex.toString(), "com.oms.order.exceptions.BadRequestException: The supplied parameter is not correct");
		}
	}

	// Testing controller function if their is an exception.
	@Test
	public void testControllerFunctionIfException() {
		try{
			ResponseEntity<OrderListResponse> response = orderController.getOrderListByBrokerId("9223372036854775808", 0, 5, null);
		} catch (Exception ex){
			orderControllerExceptionHandler.handleException(ex);
			Assert.assertEquals(ex.toString(), "java.lang.NumberFormatException: For input string: \"9223372036854775808\"");
		}
	}
}