package com.oms.order.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.oms.order.dto.OrderRequest;
import com.oms.order.dto.SearchOrdersRequest;
import com.oms.order.entities.Order;
import com.oms.order.entities.Order.Type;
import com.oms.order.entities.client.Client;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
public class SearchOrderRepositoryTests extends AbstractTransactionalTestNGSpringContextTests{
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ClientRepository clientRepository;
	@Autowired 
	SearchOrdersRepositoryImpl searchOrderRepository;
	
	private SearchOrdersRequest orderRequest=new SearchOrdersRequest ();
	 List<Client> clientList= new ArrayList<Client>();
	 Long clientId1=null;
	 Long clientId2=null;
	 Long clientId3=null;
	 
	@BeforeClass
	public void setUp() {
		// code that will be invoked when this test is instantiated
		Client client1=new Client(101L,"testuser1","test1@gmail.com");
		Client addedClient1 = clientRepository.save(client1);
		clientId1=addedClient1.getClientId();
		clientList.add(addedClient1);
		

		Client client2=new Client(102L,"testuser2","test2@gmail.com");	
		Client addedClient2 = clientRepository.save(client2);
		clientId2=addedClient2.getClientId();
		clientList.add(addedClient2);
		

		Client client3=new Client(103L,"testuser3","test3@gmail.com");
		Client addedClient3 = clientRepository.save(client3);
		clientId3=addedClient3.getClientId();
		clientList.add(addedClient3);
	}
	
	
	public SearchOrdersRequest buildSearchRequest(String email, String name, LocalDate start, LocalDate end, String stock, Type type )
	{
		orderRequest.setClientEmail(email);
		orderRequest.setClientName(name);
		orderRequest.setEndDate(end);
		orderRequest.setStartDate(start);
		orderRequest.setStock(stock);
		orderRequest.setType(type);
		return orderRequest;

	}

	@Test
	@Rollback(false)
	@Transactional
	void test_getList_all()
	{
		Order testOrder = new Order(new OrderRequest(clientId1, 780, "STD", "BUY", "LIMIT", new BigDecimal("100.0"), 500L), 136L);
		testOrder.setCreatedBy(1_00_000L);
		Order savedOrder = orderRepository.save(testOrder);
		Pageable pageable = PageRequest.of(0, 5);
		Page<Order> orderList=searchOrderRepository.getSearchOrders(buildSearchRequest("test1@gmail.com","testuser1",LocalDate.now().minusDays(1),LocalDate.now().plusDays(1),"STD",Type.valueOf("LIMIT"))
				, 1_00_000L,pageable);
		Assert.assertTrue(orderList.getContent().size()>0);
	}
	
	@Test
	@Rollback(true)
	@Transactional
	void test_getList_clientMail()
	{
	
		Order testOrder = new Order(new OrderRequest(clientId2, 780, "TDS", "BUY", "LIMIT", new BigDecimal("100.0"), 500L), 136L);
		testOrder.setCreatedBy(1_00_000L);
		Order savedOrder = orderRepository.save(testOrder);
		Pageable pageable = PageRequest.of(0, 5);
		
		Page<Order> orderList=searchOrderRepository.getSearchOrders(buildSearchRequest("test2@gmail.com",null,null,null,null,null)
				, 1_00_000L,pageable);
		Assert.assertTrue(orderList.getContent().size()>0);
		
	}

	@Test
	@Rollback(true)
	@Transactional
	void test_getList_clientName()
	{
		Order testOrder = new Order(new OrderRequest(clientId3, 780, "TDS", "BUY", "LIMIT", new BigDecimal("100.0"), 500L), 136L);
		testOrder.setCreatedBy(1_00_000L);
		Order savedOrder = orderRepository.save(testOrder);
		Pageable pageable = PageRequest.of(0, 5);
		
		Page<Order> orderList=searchOrderRepository.getSearchOrders(buildSearchRequest(null,"testuser3",null,null,null,null)
					, 1_00_000L,pageable);
		Assert.assertTrue(orderList.getContent().size()>0);
			
			
	}
	
	@Test
	@Rollback(true)
	@Transactional
	void test_getList_stock()
	{
		Order testOrder = new Order(new OrderRequest(102L, 780, "TDS", "BUY", "LIMIT", new BigDecimal("100.0"), 500L), 136L);
		testOrder.setCreatedBy(1_00_000L);
		Order savedOrder = orderRepository.save(testOrder);
		Pageable pageable = PageRequest.of(0, 5);
	SearchOrdersRequest x=buildSearchRequest(null,null,null,null,"TDS",null);
		Page<Order> orderList=searchOrderRepository.getSearchOrders(x
				, 1_00_000L,pageable);
		Assert.assertTrue(orderList.getContent().size()>0);
		
	}
	
	@Test
	@Rollback(true)
	@Transactional
	void test_getList_TypeLIMIT()
	{
		Order testOrder = new Order(new OrderRequest(102L, 780, "TDS", "BUY", "LIMIT", new BigDecimal("100.0"), 500L), 136L);
		testOrder.setCreatedBy(1_00_000L);
		Order savedOrder = orderRepository.save(testOrder);
		Pageable pageable = PageRequest.of(0, 5);
		Page<Order> orderList=searchOrderRepository.getSearchOrders(buildSearchRequest(null,null,null,null,null,Type.valueOf("LIMIT"))
				, 1_00_000L,pageable);
		Assert.assertTrue(orderList.getContent().size()>0);
		
	}
	
	@Test
	@Rollback(true)
	@Transactional
	void test_getList_TypeMARKET()
	{
		Order testOrder = new Order(new OrderRequest(102L, 780, "TDS", "BUY", "MARKET", new BigDecimal("100.0"), 500L), 136L);
		testOrder.setCreatedBy(1_00_000L);
		Order savedOrder = orderRepository.save(testOrder);
		Pageable pageable = PageRequest.of(0, 5);
		Page<Order> orderList=searchOrderRepository.getSearchOrders(buildSearchRequest(null,null,null,null,null,Type.valueOf("MARKET"))
				, 1_00_000L,pageable);
		Assert.assertTrue(orderList.getContent().size()>0);
		
	}
	
	@Test
	@Rollback(true)
	@Transactional
	void test_getList_StartEnd()
	{
		Order testOrder = new Order(new OrderRequest(102L, 780, "TDS", "BUY", "LIMIT", new BigDecimal("100.0"), 500L), 136L);
		testOrder.setCreatedBy(1_00_000L);
		Order savedOrder = orderRepository.save(testOrder);
		Pageable pageable = PageRequest.of(0, 5);
		Page<Order> orderList=searchOrderRepository.getSearchOrders(buildSearchRequest(null,null,LocalDate.now().minusDays(1),LocalDate.now().plusDays(1),null,null)
				, 1_00_000L,pageable);
		Assert.assertTrue(orderList.getContent().size()>0);
		
	}
	@Test
	@Rollback(true)
	@Transactional
	void test_getList_StartDate()
	{
		Order testOrder = new Order(new OrderRequest(102L, 780, "TDS", "BUY", "LIMIT", new BigDecimal("100.0"), 500L), 136L);
		testOrder.setCreatedBy(1_00_000L);
		Order savedOrder = orderRepository.save(testOrder);
		Pageable pageable = PageRequest.of(0, 5);
		Page<Order> orderList=searchOrderRepository.getSearchOrders(buildSearchRequest(null,null,LocalDate.now().minusDays(1),null,null,null)
				, 1_00_000L,pageable);
		Assert.assertTrue(orderList.getContent().size()>0);
		
	}
	@Test
	@Rollback(true)
	@Transactional
	void test_getList_EndDate()
	{
		Order testOrder = new Order(new OrderRequest(102L, 780, "TDS", "BUY", "LIMIT", new BigDecimal("100.0"), 500L), 136L);
		testOrder.setCreatedBy(1_00_000L);
		Order savedOrder = orderRepository.save(testOrder);
		Pageable pageable = PageRequest.of(0, 5);
		Page<Order> orderList=searchOrderRepository.getSearchOrders(buildSearchRequest(null,null,null,LocalDate.now().plusDays(1),null,null)
				, 1_00_000L,pageable);
		Assert.assertTrue(orderList.getContent().size()>0);
		
	}
	@Test
	@Rollback(true)
	@Transactional
	void test_getList_Empty()
	{
		Order testOrder = new Order(new OrderRequest(102L, 780, "TDS", "BUY", "LIMIT", new BigDecimal("100.0"), 500L), 136L);
		testOrder.setCreatedBy(1_00_000L);
		Order savedOrder = orderRepository.save(testOrder);
		Pageable pageable = PageRequest.of(0, 5);
		Page<Order> orderList=searchOrderRepository.getSearchOrders(buildSearchRequest(null,null,null,null,null,null)
				, 1_00_000L,pageable);
		Assert.assertTrue(orderList.getContent().size()>0);
		
	}
	
	
	
	@AfterClass
	public void clean()
	{
		for(Client c:clientList)
		{
			clientRepository.delete(c);
		}

	}
}
