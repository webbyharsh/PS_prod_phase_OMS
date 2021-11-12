package com.oms.order;

import static org.junit.Assert.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.oms.order.controller.OrderController;
import com.oms.order.repository.OrderRepository;
import com.oms.order.service.OrderService;

@SpringBootTest
class OrderModuleTests extends AbstractTestNGSpringContextTests{

	@Autowired
	private OrderController orderController;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderService orderService;

	@Test
	void order_controller_loaded() {
		assertNotNull(orderController);
	}

	@Test
	void order_repository_loaded() {
		assertNotNull(orderRepository);
	}

	@Test
	void order_service_loaded() {
		assertNotNull(orderService);
	}
}
