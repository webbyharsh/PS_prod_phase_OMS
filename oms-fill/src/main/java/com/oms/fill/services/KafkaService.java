package com.oms.fill.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oms.fill.dto.OrderNotification;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaService {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	private final String TOPIC;

	public KafkaService(@Value("${notification.topic}") String topic) {
		this.TOPIC = topic;
	}

	public boolean produceOrderJourneyNotification(OrderNotification notification) {
		log.info("Sending notification request for {}", notification.getEmail());
		ObjectMapper mp = new ObjectMapper();
		try {
			kafkaTemplate.send(TOPIC, mp.writeValueAsString(notification));
			return true;
		} catch (JsonProcessingException e) {
			log.error("Could not send notification request for order " + notification.getOrderId(), e);
			return false;
		}
	}
}