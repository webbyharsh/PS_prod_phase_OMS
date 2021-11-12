package com.oms.order.service;

import java.util.List;
import java.util.Optional;

import com.oms.order.configuration.AuthConfig;
import com.oms.order.dto.OrderRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.NoSuchElementException;

import com.oms.order.dto.OrderNotification;
import com.oms.order.entities.user.User;
import com.oms.order.exceptions.UserNotFoundException;
import com.oms.order.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.oms.order.entities.Order;
import com.oms.order.exceptions.OrderNotFoundException;
import com.oms.order.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService implements IOrderService {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	private AuthConfig authConfig;

	@Autowired
	UserRepository userRepository;

	@Autowired
	IKafkaService kafkaService;

	String errormessage = "Order Not Found Error";

	String ordererrormessage = "Unable to find the requested order";

	public Order saveOrder(Order newOrder) {
		// performs insert operation to save order to table and returns the saved order
		log.info("Executing Create New Order Method");
		Order savedOrder = orderRepository.save(newOrder);
		log.info("Order saved to Database");

		return savedOrder;
	}

	public Order getOrderById(Long userId, Long orderId) throws OrderNotFoundException {
		log.info(String.format("user id - %s order id - %s", userId, orderId));

		List<String> roles = authConfig.getRoles();
		Optional<Order> optionalOrder = orderRepository.findById(orderId);
		if (optionalOrder.isPresent()) {
			Order order = optionalOrder.get();
			if (order.getCreatedBy().equals(userId) || roles.contains("ROLE_ADMIN")) {
				return order;
			} else {
				log.error(errormessage);
				throw new OrderNotFoundException(ordererrormessage);
			}
		} else {
			log.error(errormessage);
			throw new OrderNotFoundException(ordererrormessage);
		}
	}

	public Order getOrderById(Long orderId) throws OrderNotFoundException {
		Optional<Order> optionalOrder = orderRepository.findById(orderId);
		if (optionalOrder.isPresent()) {
			return (optionalOrder.get());
		} else {
			log.error( errormessage);
			throw new OrderNotFoundException(ordererrormessage);
		}
	}

	public Page<Order> getOrdersByBrokerId(Long brokerId, Pageable pageable) {

		log.info("Get orders by broker id method executed");
		return orderRepository.getOrdersListByCreatedBy(brokerId, pageable);
	}

	public Order updateOrder(Order newOrderRequest, Long userId) {
		log.info("updateOrder OrderService started");
		newOrderRequest.setModifiedBy(userId);
		newOrderRequest.setModifiedAt(LocalDateTime.now());

		Order finalOrder = orderRepository.save(newOrderRequest);
		log.info("Details successfully updated in DB");
		return finalOrder;

	}

	public Order getOrderAfterRequestedUpdates(Order orderToBeUpdated, OrderRequest updateOrderRequest) {
		log.info("Updating order fields in order service");
		orderToBeUpdated.setType(updateOrderRequest.getType());
		orderToBeUpdated.setSide(updateOrderRequest.getSide());
		orderToBeUpdated.setClientId(updateOrderRequest.getClientId());
		orderToBeUpdated.setQuantity(updateOrderRequest.getQuantity());
		orderToBeUpdated.setTargetPrice(updateOrderRequest.getTargetPrice());
		orderToBeUpdated.setStock(updateOrderRequest.getStock());
		return orderToBeUpdated;
	}

	public void sendNotification(Order order) {
		User user;
		try {
			user = userRepository.findById(order.getCreatedBy())
					.orElseThrow();
		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("Requested user does not exist!");
		}
		OrderNotification notification = new OrderNotification(
				"order-" + order.getStatus().name().toLowerCase(Locale.ROOT) + "-template-v1.flth",
				user.getName(),
				user.getEmailId(),
				order.getStatus().name(),
				order.getOrderId(),
				order.getCreatedAt().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))
		);
		kafkaService.produceOrderJourneyNotification(notification);
	}
}
