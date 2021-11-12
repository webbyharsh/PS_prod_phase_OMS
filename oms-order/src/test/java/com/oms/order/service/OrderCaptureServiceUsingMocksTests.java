package com.oms.order.service;

import static org.testng.Assert.assertThrows;

import java.math.BigDecimal;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.oms.order.dto.OrderRequest;
import com.oms.order.entities.Order;
import com.oms.order.repository.OrderRepository;

@SpringBootTest
@TestExecutionListeners(MockitoTestExecutionListener.class)
class OrderCaptureServiceUsingMocksTests extends AbstractTestNGSpringContextTests{
	
	@Autowired
	private IOrderService orderService;
	
	@MockBean
	private OrderRepository orderRepository;
	
	// Database exception if unable to save data to table
	@Test
	void runtime_exception_if_cannot_save() {
		Order testOrder = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),500L), 136L);
		// Mock the repository
		// If repository is called database error will be thrown
		Mockito.when(orderRepository.save(testOrder)).thenThrow(new RuntimeException());
		
		assertThrows(RuntimeException.class, () -> orderService.saveOrder(testOrder));
	}

}
