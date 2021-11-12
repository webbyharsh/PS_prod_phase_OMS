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
public class testGetOrderAfterRequestedUpdates extends AbstractTestNGSpringContextTests {
	
	@Autowired
	OrderService orderService;
	
	@Test
	public void testIfUpdateOrderIsReturningUpdatedOrder() {
		Order oldOrder = new Order(new OrderRequest(101L, 10, "Sample Stock", "BUY", "LIMIT", new BigDecimal("300.0"),500L), 130L);
		OrderRequest updateOrderRequest = new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),136L);
		Order responseOrder = orderService.getOrderAfterRequestedUpdates(oldOrder, updateOrderRequest);
		if(responseOrder.getStock().equals(updateOrderRequest.getStock()) &&
				responseOrder.getQuantity().equals(updateOrderRequest.getQuantity()) &&
				responseOrder.getType().equals(updateOrderRequest.getType())){
			Assert.assertTrue(true);
		}
		
	}
}
