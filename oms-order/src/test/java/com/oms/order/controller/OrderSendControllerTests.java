package com.oms.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oms.order.dto.OrderRequest;
import com.oms.order.entities.Order;
import com.oms.order.service.OrderService;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@TestExecutionListeners(MockitoTestExecutionListener.class)
public class OrderSendControllerTests extends AbstractTestNGSpringContextTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Mock
    private RestTemplate restTemplate;

    @Value("${exchange_server_url}")
    private String exchangeServerURL;

    @Value("${receive_order_api}")
    private String receiveOrderAPI;

    // Will give a bad request error if no request headers
    @Test(enabled=false)
    void test_status_bad_request_if_no_request_header() throws Exception {
        mockMvc.perform(
                        put("/api/v1/order/send/{orderId}",75))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Required request header 'userId' for method parameter type String is not present")));
    }

    // Will give a bad request error if order ID can't be parsed to Long
    @Test(enabled=false)
    void test_status_bad_request_if_order_id_invalid() throws Exception {
        mockMvc.perform(
                        put("/api/v1/order/send/{orderId}","Seventy Five")
                                .header("userId", "146").header("Authorization", "blahblah"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", is("For input string: \"Seventy Five\"")));
    }

    // Will be successful when a good request received
    @Test(enabled=false)
    void test_send_order_and_status_ok_if_request_ok() throws Exception {
        Order mockedOrder = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),124L), 146L);
        Order mockedSentOrder = new Order(new OrderRequest(102L, 780, "Test Stock", "BUY", "LIMIT", new BigDecimal("100.0"),124L), 146L);
        mockedOrder.setStatus(Order.Status.valueOf("CREATED"));
        mockedOrder.setModifiedAt(LocalDateTime.now());
        mockedOrder.setCreatedAt(LocalDateTime.now());
        mockedOrder.setModifiedBy(146L);
        mockedOrder.setCreatedBy(146L);
        mockedOrder.setIsActive(true);
        mockedOrder.setStockPrice(BigDecimal.valueOf(100));
        mockedOrder.setTargetPrice(BigDecimal.valueOf(200));
        mockedOrder.setOrderId(124L);
        //Since this is a new order being saved, the status of it will be CREATED only

        // Mock fetching of order
        Mockito.when(orderService.getOrderById(Mockito.anyLong(),Mockito.anyLong())).thenReturn(mockedOrder);

        // Mock Exchange Response
        Mockito.when(restTemplate.postForObject(exchangeServerURL + receiveOrderAPI, mockedOrder, String.class)).thenReturn("RECEIVED");
//        Mockito.when(restTemplate.postForObject(Mockito.isA((String.class)), Mockito.isA(Order.class), Mockito.isA(String.class))).thenReturn("RECEIVED");

        // Mock saving of order with SENT status
        mockedSentOrder.setStatus(Order.Status.valueOf("SENT"));
        Mockito.when(orderService.saveOrder(Mockito.isA(Order.class))).thenReturn(mockedSentOrder);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/order/send/124")
                .header("userId", "146").header("Authorization", "blahblah");

        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        System.out.println(response.getContentAsString());
        Order responseToDto = new ObjectMapper().readValue(response.getContentAsString(), Order.class);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(responseToDto.getStatus().toString(), "SENT");
    }
}
