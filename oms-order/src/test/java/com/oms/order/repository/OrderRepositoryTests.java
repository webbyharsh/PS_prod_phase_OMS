package com.oms.order.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
import com.oms.order.entities.Order.Status;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
class OrderRepositoryTests extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	private OrderRepository orderRepository;

	// test for read order to table
	@Test
	@Rollback(true)
	@Transactional
	void test_read_order() {

		Order testOrder = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"), 500L), 136L);

		Order savedOrder = orderRepository.save(testOrder);
		Long orderId = savedOrder.getOrderId();
		Optional<Order> returnedOrder = orderRepository.findById(orderId);

		assertTrue(returnedOrder.isPresent());
	}

	// test for saving order to table
	@Test
	@Rollback(true)
	@Transactional
	void test_create_order() {
		Order testOrder = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"), 500L),
				136L);

		Order savedOrder = orderRepository.save(testOrder);
		Long expectedClientId = 102L;

		assertEquals(savedOrder.getClientId(), expectedClientId);
		assertNull(savedOrder.getStockPrice());
		assertNotNull(savedOrder.getCreatedAt());
		assertNotNull(savedOrder.getModifiedAt());
		assertTrue(savedOrder.getIsActive());
		assertEquals(savedOrder.getStatus(), Status.CREATED);
	}

	// test for getting all orders from the table
	@Test
	@Rollback(true)
	@Transactional
	void test_list_orders() {
		Order testOrder = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"), 500L),
				136L);

		orderRepository.save(testOrder);

		List<Order> orders = (List<Order>) orderRepository.findAll();
		assertThat(orders).size().isPositive();
	}

	// test for deleting orders from table
	@Test
	@Rollback(true)
	@Transactional
	void test_delete_orders() {
		Order testOrder = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),500L),
				136L);

		Order savedOrder = orderRepository.save(testOrder);

		Long countBefore = orderRepository.count();
		
		orderRepository.deleteById(savedOrder.getOrderId());
		Long countAfter = orderRepository.count();
		
		assertEquals(countBefore - countAfter, 1);
	}
}