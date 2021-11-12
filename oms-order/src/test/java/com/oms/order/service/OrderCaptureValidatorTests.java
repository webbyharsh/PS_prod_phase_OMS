package com.oms.order.service;

import static org.testng.Assert.assertThrows;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

//import java.util.List;

import org.testng.annotations.Test;

import com.oms.order.dto.OrderRequest;
import com.oms.order.exceptions.BadRequestException;

@SpringBootTest
class OrderCaptureValidatorTests extends AbstractTestNGSpringContextTests{
	
	@Autowired
	private OrderValidator orderValidator;

	// exception if request is null
	@Test
	void test_null_pointer_exception_if_order_null() {
		assertThrows(BadRequestException.class, () -> orderValidator.validateOrderRequest(null));
	}
	
	// exception if a required field is null
	@Test
	void test_exception_on_validate_order_if_clientid_field_null() {
		OrderRequest testOrderRequest = new OrderRequest(null, 780, "Test Stock", "Buy", "Limit", new BigDecimal("100.0"), 500L);
		assertThrows(BadRequestException.class, () -> orderValidator.validateOrderRequest(testOrderRequest));
	}
	
	//exception if order is limit and target price is null
	@Test
	void test_exception_on_validate_order_if_limit_and_target_price_null() {
		OrderRequest testOrderRequest = new OrderRequest(102L, 780, "Test Stock", "Buy", "Limit", null, 500L);
		assertThrows(BadRequestException.class, () -> orderValidator.validateOrderRequest(testOrderRequest));
	}
	
	// exception if type is market and target price is not null
	@Test
	void test_exception_on_validate_order_if_market_and_target_price_not_null() {
		OrderRequest testOrderRequest = new OrderRequest(102L, 780, "Test Stock", "Buy", "Market", new BigDecimal("100.0"), 500L);
		assertThrows(BadRequestException.class, () -> orderValidator.validateOrderRequest(testOrderRequest));
	}
	
	// exception if type is incorrect
	@Test
	void test_exception_on_validate_order_if_type_not_valid() {
		OrderRequest testOrderRequest = new OrderRequest(102L, 780, "Test Stock", "Buy", "ABC", new BigDecimal("100.0"),500L);
		assertThrows(BadRequestException.class, () -> orderValidator.validateOrderRequest(testOrderRequest));
	}
	
	//exception if side is incorrect
	@Test
	void test_exception_on_validate_order_if_side_not_valid() {
		OrderRequest testOrderRequest = new OrderRequest(102L, 780, "Test Stock", "ABC", "Limit", new BigDecimal("100.0"),500L);
		assertThrows(BadRequestException.class, () -> orderValidator.validateOrderRequest(testOrderRequest));
	}
	
	//exception if quantity is incorrect
	@Test
	void test_exception_on_validate_order_if_quantity_not_valid() {

		OrderRequest testOrderRequest = new OrderRequest(102L, -2, "Test Stock", "Buy", "Limit", new BigDecimal("100.0"), 500L);
		assertThrows(BadRequestException.class, () -> orderValidator.validateOrderRequest(testOrderRequest));
	}
	

}
