package com.oms.order.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oms.order.dto.OrderRequest;
import com.oms.order.entities.Order;
import com.oms.order.repository.OrderRepository;

@SpringBootTest
public class OrderListServiceTests extends AbstractTestNGSpringContextTests {

	@Autowired
	private IOrderService orderService;

	@Autowired
	private OrderRepository orderRepository;

	private List<Order> listOrder = new ArrayList<Order>();
	private List<Order> expectedList = new ArrayList<Order>();

	@BeforeClass
	public void setUp() {
		// code that will be invoked when this test is instantiated
		Order order1 = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),500L), 136L);
		order1.setCreatedBy(4_000_000L);
		order1.setStock("TATA");
		Order addedOrder1 = orderRepository.save(order1);
		listOrder.add(addedOrder1);
		expectedList.add(addedOrder1);

		Order order2 = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),500L), 136L);
		order2.setCreatedBy(4_000_000L);
		order2.setStock("MRF");
		Order addedOrder2 = orderRepository.save(order2);
		listOrder.add(addedOrder2);
		expectedList.add(addedOrder2);

		Order order3 = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),500L), 136L);
		order3.setCreatedBy(2_000_000L);
		order3.setStock("ITC");
		Order addedOrder3 = orderRepository.save(order3);
		listOrder.add(addedOrder3);
	}
	@AfterClass
	public void clean()
	{
		for(Order o:listOrder)
		{
			orderRepository.delete(o);
		}

	}
	// Test for checking whether service response is valid
	@Test
	public void testIfServiceResponseIsCorrect() {
		try {
            Pageable pageable = PageRequest.of(0, 5);
			Page<Order> actualList = orderService.getOrdersByBrokerId(4_000_000L, pageable);
			Assert.assertTrue(actualList.getContent().size() == 2);
		} catch (Exception e) {
			Assert.assertFalse(true);
		}

	}

	// Test for checking whether exceptions are generated in service
	@Test
	public void testIfServiceExceptionisGenerated() {
		try {
            Pageable pageable = PageRequest.of(0, 5);
			Page<Order> actualList = orderService.getOrdersByBrokerId(1L/0L, pageable);
		} catch (Exception e) {
			Assert.assertTrue(true);
		}
	}
}