package com.oms.order.service;

import com.oms.order.dto.OrderNotification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KafkaServiceMockImpl implements IKafkaService{
    public void produceOrderJourneyNotification(OrderNotification notification){
        log.info("Running mock implementation of Kafka Service");
    }
}
