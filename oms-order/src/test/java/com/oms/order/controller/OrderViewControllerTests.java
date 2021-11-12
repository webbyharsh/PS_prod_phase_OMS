package com.oms.order.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.oms.order.configuration.AuthConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import com.oms.order.dto.OrderRequest;
import com.oms.order.entities.Order;
import com.oms.order.exceptions.OrderNotFoundException;
import com.oms.order.repository.OrderRepository;
import com.oms.order.service.OrderService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OrderViewControllerTests extends AbstractTestNGSpringContextTests {

	private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private AuthConfig authConfig;

	@LocalServerPort
	private int port;

	private String baseUrl;

	Order expectedOrder;

	String orderId;

	String incorrectOrderId;

	Long orderSavedId;

	@BeforeClass
	public void preSetUp() {
		System.out.println("INIT");
		Order testOrder = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),500L),
				136L);

		Order savedOrder = orderRepository.saveAndFlush(testOrder);
		orderSavedId = savedOrder.getOrderId();
	}

	@BeforeClass
	public void setUp() {
		// code that will be invoked when this test is instantiated

		Optional<Order> readOrder = orderRepository.findById(orderSavedId);
		expectedOrder = readOrder.get();

		orderId = String.valueOf(expectedOrder.getOrderId());

		incorrectOrderId = String.valueOf(expectedOrder.getOrderId() + 10000);

		System.out.println("INIT");
		baseUrl = "http://localhost:" + String.valueOf(port) + "/api/v1/order/";
		List<String> roles = new ArrayList<>();
		roles.add("ROLE_ADMIN");
		authConfig.setRoles(roles);
		authConfig.setUserId(1L);

	}

	@Test
	public void testOrderViewControllerWhenOrderIsAvailable() {
		URI targetUrl = UriComponentsBuilder.fromUriString(baseUrl + orderId).build().encode().toUri();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "JWT string");
		headers.set("userId", "1");
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		ResponseEntity<Order> response = restTemplate.exchange(targetUrl, HttpMethod.GET, entity, Order.class);

		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
		System.out.println(response.getBody());
	}

	@Test
	public void testObjectInOrderViewControllerWhenOrderIsAvailable() throws OrderNotFoundException {
		URI targetUrl = UriComponentsBuilder.fromUriString(baseUrl + orderId).build().encode().toUri();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "JWT string");
		headers.set("userId", "1");
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		ResponseEntity<Order> response = restTemplate.exchange(targetUrl, HttpMethod.GET, entity, Order.class);

		Order expected = orderService.getOrderById(expectedOrder.getCreatedBy(), expectedOrder.getOrderId());

		Assert.assertSame(response.getBody().getOrderId(),expected.getOrderId());
		System.out.println(response.getBody());
	}
	
	@Test
	@Ignore
	public void testErrorInOrderViewControllerWhenAuthorizationIsEmpty() {
		URI targetUrl = UriComponentsBuilder.fromUriString(baseUrl + orderId).build().encode().toUri();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "");
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		try {
			ResponseEntity<Order> response = restTemplate.exchange(targetUrl, HttpMethod.GET, entity, Order.class);
			Assert.assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
		} catch (HttpClientErrorException e) {
			System.out.println(e.getResponseBodyAsString());
		}
	}

	@Test
	public void testOrderViewControllerWhenOrderIsNotAvailable() {
		URI targetUrl = UriComponentsBuilder.fromUriString(baseUrl + incorrectOrderId).build().encode().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "JWT string");
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		try {
			ResponseEntity<Order> response = restTemplate.exchange(targetUrl, HttpMethod.GET, entity, Order.class);
			Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
		} catch (HttpClientErrorException e) {
			System.out.println(e.getResponseBodyAsString());
		}

	}

}