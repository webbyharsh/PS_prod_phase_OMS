package com.oms.fill.services;

import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oms.fill.entities.Order;
import com.oms.fill.entities.client.Client;
import com.oms.fill.entities.user.Address;
import com.oms.fill.entities.user.User;
import com.oms.fill.repository.ClientRepository;
import com.oms.fill.repository.OrderRepository;
import com.oms.fill.repository.UserRepository;

@SpringBootTest
class NOtificationTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private NotificationService ns;
	private Fill fill;
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
		fill = Fill.FULLYFILLED;
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

	// exception if request is null
	@Test
	void test_null_exception_if_order_null_in_sendnotificatiotoBroker() {
		assertThrows(NullPointerException.class, () -> ns.sendNotificationToBroker(null, fill));
	}

	@Test
	void test_null_exception_if_order_null_in_sendnotificatiotoClient() {
		assertThrows(NullPointerException.class, () -> ns.sendNotificationToClient(null, fill));
	}

	@Test
	void test__if_user_present_in_sendnotificatiotoBroker() {
		assertTrue(ns.sendNotificationToBroker(testOrder, fill));
	}

	@Test
	void test__if_client_present_in_sendnotificatiotoClient() {
		assertTrue(ns.sendNotificationToClient(testOrder, fill));
	}

}