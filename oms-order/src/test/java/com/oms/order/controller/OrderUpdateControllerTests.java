package com.oms.order.controller;

import java.math.BigDecimal;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oms.order.dto.OrderRequest;
import com.oms.order.entities.Order;
import com.oms.order.exceptions.BadRequestException;
import com.oms.order.exceptions.OrderNotFoundException;
import com.oms.order.repository.OrderRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderUpdateControllerTests  extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderController orderController;
@Ignore
    @Test
    void testUpdateEndPointOk() throws Exception {
        Order order1 = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),500L), 136L);
        Order addedOrder1 = orderRepository.saveAndFlush(order1);
        OrderRequest updateOrderRequest = new OrderRequest(102L, 150, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"), order1.getOrderId());

        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(updateOrderRequest);
            System.out.println("ResultingJSONstring = " + json);
            //System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();}
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/order/update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("userId", order1.getOrderId().toString())
                .content(json);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Order order = new ObjectMapper().readValue(response.getContentAsString(), Order.class);
        Assert.assertEquals(response.getStatus(),202);
        Assert.assertEquals(response,order1.getQuantity());

    }

    @Test
    void testUpdateControllerMethod() throws OrderNotFoundException, BadRequestException {

        Order order1 = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),500L), 136L);
        Order addedOrder1 = orderRepository.saveAndFlush(order1);
        OrderRequest updateOrderRequest = new OrderRequest(102L, 150, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"), order1.getOrderId());


        ResponseEntity<Order> responseEntity=orderController.updateOrderById(updateOrderRequest,order1.getOrderId().toString(),order1.getCreatedBy(), "abc");


        System.out.println(responseEntity.getBody());
        System.out.println(order1);
        Assert.assertEquals(responseEntity.getBody(),order1);
    }

    // Will give a bad request error if request body is not json
    @Test
    void testBadRequestExceptionUpdateController() throws Exception {
        try{
            Order order1 = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),500L), 136L);
            Order addedOrder1 = orderRepository.saveAndFlush(order1);
            String postUpdateOrderJSON = "{\"clientId\":102,\"quantity\": 150,\"stock\": \"Test Stock\",\"side\": \"BUY\",\"type\": \"LIMIT\",\"targetPrice\": 100}";
            order1.setQuantity(150);
            RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/order/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("userId", order1.getOrderId().toString());

            MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();}
        catch (BadRequestException e)
        {
            Assert.assertEquals(e.toString(), "com.oms.order.exceptions.BadRequestException: The supplied parameter is not correct");

        }
    }

}
