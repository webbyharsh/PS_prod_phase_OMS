package com.oms.fill.services;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oms.fill.dto.OrderNotification;
import com.oms.fill.entities.Order;
import com.oms.fill.entities.client.Client;
import com.oms.fill.entities.user.User;
import com.oms.fill.repository.ClientRepository;
import com.oms.fill.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	KafkaService kafkaService;
	@Autowired
	ClientRepository clientRepository;

	public boolean sendNotificationToBroker(Order order, Fill fill) {
		User user;

		log.info(String.format("Feching User for order id - %s", order.getOrderId()));
		user = userRepository.findById(order.getCreatedBy()).orElseThrow();

		log.info(String.format("Sending Notification to order id - %s", user.getUserId()));
		OrderNotification notification = new OrderNotification("order-fill-template-v1.flth", user.getName(),
				user.getEmailId(), fill.name(), order.getOrderId(),
				order.getCreatedAt().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
		boolean check = kafkaService.produceOrderJourneyNotification(notification);
		if (check) {
			return true;
		}
		return false;
	}

	public boolean sendNotificationToClient(Order order, Fill fill) {
		Client client;

		log.info(String.format("Feching Client for order id - %s", order.getOrderId()));
		client = clientRepository.findById(order.getClientId()).orElseThrow();

		log.info(String.format("Sending Notification to client id - %s", client.getClientId()));
		OrderNotification notification = new OrderNotification("order-fill-template-v1.flth", client.getName(),
				client.getEmailId(), fill.name(), order.getOrderId(),
				order.getCreatedAt().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
		boolean check = kafkaService.produceOrderJourneyNotification(notification);
		if (check) {
			return true;
		}
		return false;
	}

}
