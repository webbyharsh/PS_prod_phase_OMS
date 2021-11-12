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
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.web.client.RestTemplate;
import com.oms.order.exceptions.BadRequestException;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderAcknowledgementControllerTests extends AbstractTransactionalTestNGSpringContextTests {

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
    public void testAcknowledgeOrderByIdIfResponse() throws Exception {
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
        Order order1 = new Order(new OrderRequest(102L, 781, "Test Stock2", "BUY", "LIMIT", new BigDecimal("100.0"),500L), savedUser.getUserId());
        Order addedOrder1 = orderRepository.save(order1);
        ResponseEntity<Order> response = orderController.acknowledgeOrderFromExchange(addedOrder1.getOrderId(), "ACCEPTED", "Order accepted from exchange");
        Assert.assertEquals(response.getBody().getStatus().toString(),"ACCEPTED");
    }


    @Test
    void testBadRequestExceptionAcknowledgeController() throws Exception {
        try{
            Order order1 = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),500L), 136L);
            Order addedOrder1 = orderRepository.save(order1);
            orderController.acknowledgeOrderFromExchange(addedOrder1.getOrderId(),"REJECTED", "Rejected in exchange");
            orderController.acknowledgeOrderFromExchange(addedOrder1.getOrderId(),"ACCEPTED", "Order accepted in exchange");
        } catch (BadRequestException e)
        {
            Assert.assertEquals(e.toString(), "com.oms.order.exceptions.BadRequestException: The status of already rejected order can not be changed");

        }
    }

    @Test
    void testBadRequestExceptionCancelAcknowledgeController() throws Exception {
        try{
            Order order1 = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),500L), 136L);
            Order addedOrder1 = orderRepository.save(order1);
            orderController.cancelOrderById(addedOrder1.getOrderId().toString(),addedOrder1.getCreatedBy(), "abc");
            orderController.acknowledgeOrderFromExchange(addedOrder1.getOrderId(),"ACCEPTED", "Order accepted in exchange");
        } catch (BadRequestException e)
        {
            Assert.assertEquals(e.toString(), "com.oms.order.exceptions.BadRequestException: The status of already rejected order can not be changed");

        }
    }

    @Test
    void testNotFoundExceptionAckController() throws Exception {
        Order order1 = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),500L), 136L);
        Order addedOrder1 = orderRepository.save(order1);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/order/ack")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("OrderId", order1.getOrderId() + 1)
                .header("Status", "ACCEPTED")
                .header("Reason", "Order accepted at exchange");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assert.assertEquals(response.getStatus(),404);

    }

}


