package com.oms.order.service;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;

//import java.util.List;

import org.testng.annotations.Test;

import com.oms.order.dto.OrderRequest;
import com.oms.order.entities.Order;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class OrderCaptureServiceTests extends AbstractTransactionalTestNGSpringContextTests{

	@Autowired
	private IOrderService orderService;
	
	// Test order service
	// Tests if service return object of desired type
	// Tests if data in object is as expected
	@Test
	@Rollback(true)
	@Transactional
	void test_create_order_if_request_ok() {
		Order testOrder = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),500L), 136L);

		Order savedOrder = orderService.saveOrder(testOrder);

		assertNotNull(savedOrder);
		Long expectedClientId = 102L;
		BigDecimal expectedTargetPrice = new BigDecimal("100.0");
		String expectedStock = "Test Stock";

		assertEquals(savedOrder.getClientId(), expectedClientId);
		assertEquals(savedOrder.getTargetPrice(), expectedTargetPrice);
		assertEquals(savedOrder.getStock(), expectedStock);
	}
}