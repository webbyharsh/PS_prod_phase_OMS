package com.oms.order.service;

import static org.junit.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oms.order.dto.OrderRequest;
import com.oms.order.entities.Order;
import com.oms.order.exceptions.OrderNotFoundException;
import com.oms.order.repository.OrderRepository;

@SpringBootTest
public class testGetOrderByIdWithTwoParameters extends AbstractTestNGSpringContextTests {

	@Autowired
	OrderService orderService;

	@Autowired
	OrderRepository orderRepository;

	Order expectedOrder;

	Long falseOrderId;

	Long orderId;
	Long userId;

	@BeforeClass
	public void setup() {
		System.out.println("INIT");
		Order testOrder = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),500L),
				136L);

		Order savedOrder = orderRepository.saveAndFlush(testOrder);

		orderId = savedOrder.getOrderId();
		userId = savedOrder.getCreatedBy();

		expectedOrder = savedOrder;

		falseOrderId = savedOrder.getOrderId() + 10000;

	}

	// When correct detail is passed.
	@Test
	@Rollback(true)
	@Transactional
	public void order_returned_equal_given_correct_details() throws OrderNotFoundException {

		Order actualOrder = orderService.getOrderById(userId, orderId);

		assertEquals(actualOrder.getOrderId(), expectedOrder.getOrderId());
	}

	// When incorrect user ID is passed.
	@Test
	@Rollback(true)
	@Transactional
	public void order_not_found_given_incorrect_user_id() {
		Exception expectedError = null;
		try {
			Order actual_order = orderService.getOrderById(userId + 1, orderId);
		} catch (Exception e) {
			expectedError = e;
		}
		assertNotNull(expectedError);
		assertTrue(expectedError instanceof OrderNotFoundException);
	}

	// When incorrect order ID is passed.
	@Test
	public void order_not_found_given_incorrect_order_id() {

		Exception expectedError = null;
		try {
			Order actual_order = orderService.getOrderById(userId, falseOrderId);
			// editor will show error , ignore it and run the test file
		} catch (Exception e) {
			expectedError = e;
		}
		assertNotNull(expectedError);
		assertTrue(expectedError instanceof OrderNotFoundException);

	}

}
