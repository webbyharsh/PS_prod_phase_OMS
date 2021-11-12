package com.oms.exchange.service;

import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

//import java.util.List;

import org.testng.annotations.Test;

import com.oms.exchange.dto.OrderRequest;
import com.oms.exchange.dto.OrderResponse;

@SpringBootTest
class ExchangeServiceTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private IExchangeService orderService;

	@Test
	void test_if_quantity_is_larger_than_1000() {
		OrderRequest testrequest = new OrderRequest(1L, 102L, 9000, "Zovio", "BUY", "LIMIT", new BigDecimal("100.0"));
		try {
			CompletableFuture<OrderResponse> dp = orderService.exchangeService(testrequest);
			assertEquals(dp.get().getStatus(), "REJECTED");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void test_if_limit_present_but_not_targetprice() {
		OrderRequest testrequest = new OrderRequest(1L, 102L, 700, "Zovio", "BUY", "LIMIT", null);

		try {
			CompletableFuture<OrderResponse> dp = orderService.exchangeService(testrequest);
			assertEquals(dp.get().getStatus(), "REJECTED");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	void test_if_targetprice_negative() {
		OrderRequest testrequest = new OrderRequest(1L, 102L, 700, "Zovio", "BUY", "LIMIT", new BigDecimal("-100.0"));

		try {
			CompletableFuture<OrderResponse> dp = orderService.exchangeService(testrequest);
			assertEquals(dp.get().getStatus(), "REJECTED");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	void test_request_is_correct() {
		OrderRequest testrequest = new OrderRequest(1L, 102L, 700, "Zovio", "BUY", "LIMIT", new BigDecimal("100.0"));

		try {
			CompletableFuture<OrderResponse> dp = orderService.exchangeService(testrequest);
			assertEquals(dp.get().getStatus(), "ACCEPTED");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	void test_if_stock_is_not_present() {
		OrderRequest testrequest = new OrderRequest(1L, 102L, 90, "TestStock", "BUY", "LIMIT", new BigDecimal("100.0"));

		try {
			CompletableFuture<OrderResponse> dp = orderService.exchangeService(testrequest);
			assertEquals(dp.get().getStatus(), "REJECTED");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
