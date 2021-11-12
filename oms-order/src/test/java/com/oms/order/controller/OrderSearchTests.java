package com.oms.order.controller;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import java.util.ArrayList;

import java.util.List;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;


import com.oms.order.dto.OrderListResponse;
import com.oms.order.dto.OrderRequest;
import com.oms.order.dto.SearchOrdersRequest;
import com.oms.order.entities.Order;
import com.oms.order.exceptions.BadRequestException;
import com.oms.order.exceptions.OrderNotFoundException;
import com.oms.order.service.SearchOrderService;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;


@SpringBootTest
@AutoConfigureMockMvc
@TestExecutionListeners(MockitoTestExecutionListener.class)
//@ActiveProfiles("test")

public class OrderSearchTests extends AbstractTestNGSpringContextTests{

	@Autowired
	private OrderController orderController;

	@MockBean
	private SearchOrderService searchOrderService;
	
	@Autowired
	private MockMvc mockMvc;
	public List<Order> orderList = new ArrayList<Order>();

	public Pageable pageable;
	
	public Page<Order> pageOrder;
	public SearchOrdersRequest orderRequest=new SearchOrdersRequest ();
	

	@BeforeClass
	public void setUp() throws Exception {
		Order order1 = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),500L), 136L);
		orderList.add(order1);
		Order order2 = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),500L), 136L);
		orderList.add(order2);
		Order order3 = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),500L), 136L);
		orderList.add(order3);
		buildSearchRequest();
		pageOrder=orderListFunction();
	}

	@Test
	public void searchOrderOk() throws BadRequestException, OrderNotFoundException
	{
		
		Mockito.when(searchOrderService.getSearchOrders(Mockito.isA(SearchOrdersRequest.class),Mockito.anyLong(),Mockito.isA(Pageable.class))).thenReturn(pageOrder);
		ResponseEntity<OrderListResponse> response=orderController.searchOrders(orderRequest, "JWT",Long.valueOf(1),Integer.valueOf(0),Integer.valueOf(2));
		
		Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
			
	}
	
	@Test
	public void searchOrdersInternalServerError() throws Exception
	{
		Mockito.when(searchOrderService.getSearchOrders(Mockito.isA(SearchOrdersRequest.class),Mockito.anyLong(),Mockito.isA(Pageable.class))).thenThrow(new RuntimeException());
		ResponseEntity<OrderListResponse> response=orderController.searchOrders(orderRequest, "JWT",Long.valueOf(1),Integer.valueOf(0),Integer.valueOf(2));
		
		Assert.assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Test
	public void searchOrdersUnauthorized() throws Exception {
		Mockito.when(searchOrderService.getSearchOrders(Mockito.isA(SearchOrdersRequest.class),Mockito.anyLong(),Mockito.isA(Pageable.class))).thenReturn(pageOrder);
		ResponseEntity<OrderListResponse> response=orderController.searchOrders(orderRequest, "",Long.valueOf(1),Integer.valueOf(0),Integer.valueOf(2));
		
		Assert.assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
	}
	
	@Test
	public void searchOrderBadRequestError() throws Exception
	{
	
		String jsont="{\"stock\":\"test\",\"type\":\"Limit\",\"startDate\":\"\",\"endDate\":\"\",\"clientEmail\":\"\",\"clientName\":\"\"}";

		this.mockMvc
				.perform(put("/api/v1/search-order").header("userId","").header("Authorization", "jwt")
					.header("pageIndex",0).header("pageSize",3)
					.contentType(MediaType.APPLICATION_JSON).content(jsont))
				.andDo(print()).andExpect(status().isBadRequest()).andDo(print());
	}
	
	@Test	
	public void searchOrderNotFoundError() throws Exception
	{
		Page<Order> pageTest=new PageImpl<Order>(new ArrayList<Order>(), pageable, 0);
		Mockito.when(searchOrderService.getSearchOrders(Mockito.isA(SearchOrdersRequest.class),Mockito.anyLong(),Mockito.isA(Pageable.class))).thenReturn(pageTest);
		ResponseEntity<OrderListResponse> response=orderController.searchOrders(orderRequest, "JWT",Long.valueOf(1),Integer.valueOf(0),Integer.valueOf(2));
		
		Assert.assertEquals(response.getBody(),null);
		
	}
	
	
		
	public void buildSearchRequest()
	{
		orderRequest.setClientEmail("abc@gmailcom");
		orderRequest.setClientName("test1");
		orderRequest.setEndDate(null);
		orderRequest.setStartDate(null);
		orderRequest.setStock("testStock1");
		orderRequest.setType(null);
	
	}

	public Page<Order> orderListFunction()
	{
		pageable=PageRequest.of(Integer.valueOf(0),Integer.valueOf(5));
		int count = pageable.getPageSize();
		int page = pageable.getPageNumber();
        int max = (count*(page+1)>orderList.size())? orderList.size(): count*(page+1);
        Page <Order> x=new PageImpl<Order>(orderList.subList(page*count, max), pageable, orderList.size());
       
        return x;

	}
}
