package com.oms.fill.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
//import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oms.fill.dto.FillRequest;
import com.oms.fill.entities.Order;
import com.oms.fill.entities.client.Client;
import com.oms.fill.entities.user.Address;
import com.oms.fill.entities.user.User;
import com.oms.fill.exceptions.OrderNotFoundException;
import com.oms.fill.repository.ClientRepository;
import com.oms.fill.repository.OrderRepository;
import com.oms.fill.repository.UserRepository;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@SpringBootTest
class UpdateOrderServiceTest extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	private UpdateOrderService updateorderService;
	private Order testOrder;
	private User testUser;
	private Client testClient;
	@Autowired
	private OrderRepository or;
	@Autowired
	private ClientRepository cr;
	@Autowired
	private UserRepository ur;

	@BeforeClass
	public void setup() {

		LocalDateTime time = LocalDateTime.now();

		Address address = new Address("Test Street", "Test City", "Test State", "Test Country");
		testUser = new User("gaurav", "test037@gmail.com", true, "1233", "1garu3", true, time, time, "1234567890",
				address, 25);
		testUser = ur.save(testUser);
		testClient = new Client("gaurav", "test789@gmail.com", time, 21L, time, 21L, address, "1234567890", true);
		testClient = cr.save(testClient);
		testOrder = new Order(testClient.getClientId(), 100, "Zovio", new BigDecimal(123.0), new BigDecimal(123.0),
				time, time, testUser.getUserId(), testUser.getUserId(), true, time, 340, new BigDecimal(100.0), 1L,
				Order.Status.ACCEPTED, Order.Side.BUY, Order.Type.LIMIT);
		testOrder = or.save(testOrder);

	}

	@AfterClass
	public void clean() {
		or.delete(testOrder);
		cr.delete(testClient);
		ur.delete(testUser);
	}

	@Test
	@Rollback(true)
	@Transactional
	void test_update_order_if_request_ok() throws Exception {
		LocalDateTime time = LocalDateTime.now();
		FillRequest testFillRequest = new FillRequest(testOrder.getOrderId(), 1l, 30, "Test Stock",
				new BigDecimal("100.0"), time);

		Order order = updateorderService.updateOrder(testFillRequest);

		assertNotNull(testFillRequest);

		Integer expcquantity = 30;
		BigDecimal expcexecutedPrice = new BigDecimal("100.0");
		LocalDateTime expcexecutedTime = time;
		Long expcexchangeid = 1l;

		assertEquals(expcquantity, order.getQuantityFilled());
		assertEquals(expcexecutedPrice, order.getExecutedPrice());
		assertEquals(expcexecutedTime, order.getExecuteAt());
		assertEquals(expcexchangeid, order.getExchangeId());

	}

	@Test
	@Rollback(true)
	@Transactional
	void test_update_order_if_order_not_found() throws Exception {
		LocalDateTime time = LocalDateTime.now();
		FillRequest testFillRequest = new FillRequest(0l, 1l, 30, "Amazon", new BigDecimal("100.0"), time);

		assertThrows(OrderNotFoundException.class, () -> updateorderService.updateOrder(testFillRequest));

	}
}