package com.oms.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oms.order.dto.OrderNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
public class KafkaService implements IKafkaService{

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${notification.topic}")
    private String TOPIC;



    public void produceOrderJourneyNotification(OrderNotification notification) {
        log.info("Sending notification request for {}", notification.getEmail());
        ObjectMapper mp = new ObjectMapper();
        try {
            kafkaTemplate.send(TOPIC, mp.writeValueAsString(notification));
        } catch (JsonProcessingException e) {
            log.error("Could not send notification request for order " +
                    notification.getOrderId(), e);
        }
    }
}
