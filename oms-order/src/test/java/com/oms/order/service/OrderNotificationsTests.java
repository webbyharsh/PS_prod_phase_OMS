package com.oms.order.service;

import com.oms.order.dto.OrderNotification;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.SettableListenableFuture;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

@Ignore("Unable to mock KafkaTemplate properly")
@SpringBootTest
@TestExecutionListeners(MockitoTestExecutionListener.class)
public class OrderNotificationsTests {

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private IKafkaService kafkaService;

    @Test
    void test_service_produces_message() {
        OrderNotification notification = new OrderNotification(
            "test-template.flth",
            "TestUser",
            "testEmail@testDomain.com",
            "created",
            1L,
            "Test Timestamp"
        );

        SettableListenableFuture<SendResult<String, String>> future = new SettableListenableFuture<>();
        Mockito.when(kafkaTemplate.send(Mockito.anyString(), Mockito.anyString())).thenReturn(future);
        kafkaService.produceOrderJourneyNotification(notification);
    }
}
