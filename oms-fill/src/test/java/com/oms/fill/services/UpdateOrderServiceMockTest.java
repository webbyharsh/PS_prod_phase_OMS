package com.oms.fill.services;

import static org.testng.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

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
import com.oms.fill.repository.OrderRepository;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestExecutionListeners(MockitoTestExecutionListener.class)
class UpdateOrderServMockTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private UpdateOrderService uds;
	@MockBean
	private OrderRepository or;

	@Test
	void test_sendnotification_toBroker_if_request_ok() throws Exception {
		LocalDateTime time = LocalDateTime.now();
		Order testOrder = new Order(188L, 2L, 100, "Zovio", new BigDecimal(123.0), new BigDecimal(123.0), time, time,
				21L, 1L, true, time, 3, new BigDecimal(100.0), 1L, Order.Status.ACCEPTED, Order.Side.BUY,
				Order.Type.LIMIT);
		FillRequest testFillRequest = new FillRequest(188L, 2L, 30, "Zovio", new BigDecimal("100.0"), time);
		Optional<Order> testOptionalOrder = Optional.of(testOrder);

		Mockito.when(or.findById(Mockito.isA(Long.class))).thenReturn(testOptionalOrder);
		Mockito.when(or.save(Mockito.isA(Order.class))).thenReturn(testOrder);

		Order orderFilled = uds.updateOrder(testFillRequest);
		assertEquals(orderFilled.getOrderId(), testFillRequest.getOrderId());
		assertEquals(orderFilled.getQuantityFilled(), testFillRequest.getQuantity());
		assertEquals(orderFilled.getExecutedPrice(), testFillRequest.getExecutedPrice());
		assertEquals(orderFilled.getStock(), testFillRequest.getStock());

	}

}