package com.oms.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oms.order.dto.OrderRequest;
import com.oms.order.entities.Order;
import com.oms.order.entities.user.Address;
import com.oms.order.entities.user.User;
import com.oms.order.repository.OrderRepository;
import com.oms.order.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.web.client.RestTemplate;
import com.oms.order.exceptions.BadRequestException;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderCancelControllerTests extends AbstractTransactionalTestNGSpringContextTests {

    private RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    OrderController orderController;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
	OrderControllerExceptionHandler orderControllerExceptionHandler;
    @Autowired
    UserRepository userRepository;
   

    //Testing function if status is changed to cancelled
    @Test
    public void testCancelOrderByIdIfResponse() throws Exception {
        User user1 = new User();
        user1.setName("abcde");
        user1.setEmailId("agrox@gmail.com");
        user1.setPassword("p@55worD");
        user1.setContact("1009112312");
        user1.setAge(22);
        user1.setAddress(new Address("stree", "city", "state", "country"));
        user1.setCreatedAt(LocalDateTime.now());
        user1.setEnabled(true);
        user1.setLastActiveAt(LocalDateTime.now());
        user1.setVerificationCode("");
        User savedUser = userRepository.save(user1);
        Order order1 = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),500L), savedUser.getUserId());
        Order addedOrder1 = orderRepository.save(order1);
        ResponseEntity<Order> response = orderController.cancelOrderById(addedOrder1.getOrderId().toString(),addedOrder1.getCreatedBy(), "abc");
        Assert.assertEquals(response.getBody().getStatus().toString(),"CANCELLED");
    }

    // Testing function if their is bad request exception
    @Test
	public void testCancelOrderFunctionIfBadRequestException(){
		try{
            Order order1 = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),500L), 136L);
            Order addedOrder1 = orderRepository.save(order1);
			ResponseEntity<Order> response = orderController.cancelOrderById(addedOrder1.getOrderId().toString(),addedOrder1.getCreatedBy(), "abc");
		} catch (BadRequestException ex){
			orderControllerExceptionHandler.handleException(ex);
			Assert.assertEquals(ex.toString(), "com.oms.order.exceptions.BadRequestException: The supplied parameter is not correct");
		}
	}

    //Testing function if their is an exception
    @Test
	public void testCancelOrderFunctionIfException() {
		try{
            Order order1 = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),500L), 136L);
            Order addedOrder1 = orderRepository.save(order1);
			ResponseEntity<Order> response = orderController.cancelOrderById(addedOrder1.getOrderId().toString(),addedOrder1.getCreatedBy(), "abc");
		} catch (Exception ex){
			orderControllerExceptionHandler.handleException(ex);
			Assert.assertEquals(ex.toString(), "java.lang.NumberFormatException");
		}
	}
    @Test
    void testBadRequestExceptionCancelController() throws Exception {
        try{
            Order order1 = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),500L), 136L);
            Order addedOrder1 = orderRepository.save(order1);
            orderController.cancelOrderById(addedOrder1.getOrderId().toString(),addedOrder1.getCreatedBy(), "abc");
            orderController.cancelOrderById(addedOrder1.getOrderId().toString(),addedOrder1.getCreatedBy(), "abc");
        } catch (BadRequestException e)
        {
            Assert.assertEquals(e.toString(), "com.oms.order.exceptions.BadRequestException: The status of order can not be cancelled");

        }
    }
    @Test
    void testNotFoundExceptionCancelController() throws Exception {
        Order order1 = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),500L), 136L);
        Order addedOrder1 = orderRepository.save(order1);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/order/550")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("userId", order1.getOrderId().toString())
                .header("Authorization", "abc");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assert.assertEquals(response.getStatus(),404);

    }

}

