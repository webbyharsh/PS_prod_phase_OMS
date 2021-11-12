package com.oms.order.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.oms.order.dto.OrderRequest;
import com.oms.order.entities.Order;

@SpringBootTest
public class OrderUpdateServiceTests extends AbstractTestNGSpringContextTests{
	
	@Autowired
	private IOrderService orderService;
	
	@Test
	public void testIfUpdateOrderIsReturningUpdatedOrder() {
		Order order1 = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),500L), 136L);
		Long userId = 000L;
		Order responseOrder = orderService.updateOrder(order1, userId);
		Assert.assertEquals(responseOrder.getModifiedBy(), userId);
	}
}
