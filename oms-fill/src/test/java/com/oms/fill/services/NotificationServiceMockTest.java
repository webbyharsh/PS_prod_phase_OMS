package com.oms.fill.services;

import static org.testng.Assert.assertTrue;

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

import com.oms.fill.dto.OrderNotification;
import com.oms.fill.entities.Order;
import com.oms.fill.entities.client.Client;
import com.oms.fill.entities.user.Address;
import com.oms.fill.entities.user.User;
import com.oms.fill.repository.ClientRepository;
import com.oms.fill.repository.OrderRepository;
import com.oms.fill.repository.UserRepository;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestExecutionListeners(MockitoTestExecutionListener.class)
class NotificationServMockTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private NotificationService ns;
	@MockBean
	private OrderRepository or;
	@MockBean
	private ClientRepository cr;
	@MockBean
	private UserRepository ur;
	@MockBean
	private KafkaService ks;

	@Test
	void test_sendnotification_toBroker_if_request_ok() throws Exception {
		Order testOrder = new Order();
		LocalDateTime time = LocalDateTime.now();
		testOrder.setOrderId(188L);
		testOrder.setClientId(2L);
		testOrder.setCreatedBy(21L);
		testOrder.setQuantity(500);
		testOrder.setStatus(Order.Status.ACCEPTED);
		testOrder.setCreatedAt(time);
		Address address = new Address("Test Street", "Test City", "Test State", "Test Country");
		User testUser = new User(21L, "gaurav", "test@gmail.com", true, "1233", "1garu3", true, time, time,
				"1234567890", address, 25, null);
		Optional<User> testOptionalUser = Optional.of(testUser);
		Mockito.when(ur.findById(Mockito.isA(Long.class))).thenReturn(testOptionalUser);
		Mockito.when(ks.produceOrderJourneyNotification(Mockito.isA(OrderNotification.class))).thenReturn(true);

		boolean orderFilled = ns.sendNotificationToBroker(testOrder, Fill.FULLYFILLED);
		assertTrue(orderFilled);
	}

	@Test
	void test_sendnotification_toClient_if_request_ok() throws Exception {
		Order testOrder = new Order();
		LocalDateTime time = LocalDateTime.now();
		testOrder.setOrderId(188L);
		testOrder.setClientId(2L);
		testOrder.setCreatedBy(21L);
		testOrder.setQuantity(500);
		testOrder.setStatus(Order.Status.ACCEPTED);
		testOrder.setCreatedAt(time);
		Address address = new Address("Test Street", "Test City", "Test State", "Test Country");
		Client testClient = new Client(2L, "gaurav", "test@gmail.com", time, 21L, time, 21L, address, "1234567890",
				true);
		Optional<Client> testOptionalClient = Optional.of(testClient);
		Mockito.when(cr.findById(Mockito.isA(Long.class))).thenReturn(testOptionalClient);
		Mockito.when(ks.produceOrderJourneyNotification(Mockito.isA(OrderNotification.class))).thenReturn(true);

		boolean orderFilled = ns.sendNotificationToClient(testOrder, Fill.FULLYFILLED);
		assertTrue(orderFilled);
	}

}