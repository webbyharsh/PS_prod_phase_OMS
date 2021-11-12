package com.oms.fill.services;

import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

//import java.util.List;

import org.testng.annotations.Test;

import com.oms.fill.dto.FillRequest;
import com.oms.fill.entities.Order;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestExecutionListeners(MockitoTestExecutionListener.class)
class FillOrderMockTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private FillService fillService;
	@MockBean
	private UpdateOrderService uds;
	@MockBean
	private NotificationService ns;

	@Test
	void test_fill_order_if_request_ok() throws Exception {
		LocalDateTime time = LocalDateTime.now();
		FillRequest testFillRequest = new FillRequest(188L, 1L, 30, "Amazon", new BigDecimal("100.0"), time);
		Order testOrder = new Order();
		testOrder.setOrderId(1L);
		testOrder.setClientId(1L);
		testOrder.setCreatedBy(2L);
		testOrder.setQuantity(500);
		Mockito.when(uds.updateOrder(Mockito.isA(FillRequest.class))).thenReturn(testOrder);
		Mockito.when(ns.sendNotificationToBroker(Mockito.isA(Order.class), Mockito.isA(Fill.class))).thenReturn(true);
		Mockito.when(ns.sendNotificationToClient(Mockito.isA(Order.class), Mockito.isA(Fill.class))).thenReturn(true);
		boolean orderFilled = fillService.fillOrder(testFillRequest);
		assertTrue(orderFilled);
	}

}