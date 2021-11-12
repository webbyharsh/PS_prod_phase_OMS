package com.oms.order.service;

import com.oms.order.dto.OrderNotification;
import org.springframework.beans.factory.annotation.Value;

public interface IKafkaService {
    public void produceOrderJourneyNotification(OrderNotification notification);

}
