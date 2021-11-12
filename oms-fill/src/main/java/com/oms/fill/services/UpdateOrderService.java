package com.oms.fill.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oms.fill.dto.FillRequest;
import com.oms.fill.entities.Order;
import com.oms.fill.exceptions.OrderNotFoundException;
import com.oms.fill.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UpdateOrderService {
	@Autowired
	OrderRepository orderRepository;

	public Order updateOrder(FillRequest rq) throws OrderNotFoundException {
		log.info(String.format("order id - %s executed Price - %s executed Quantity - %s executed Time - %s",
				rq.getOrderId(), rq.getExecutedPrice(), rq.getQuantity(), rq.getExecutedTime()));

		Optional<Order> optionalOrder = orderRepository.findById(rq.getOrderId());
		if (optionalOrder.isPresent()) {
			Order order = optionalOrder.get();
			order.setQuantityFilled(rq.getQuantity());
			order.setExecuteAt(rq.getExecutedTime());
			order.setExchangeId(rq.getExchangeId());
			order.setExecutedPrice(rq.getExecutedPrice());
			orderRepository.save(order);
			log.info(String.format("order id - %s updated in db", rq.getOrderId()));
			return order;

		} else {
			log.error("Order Not Found Error");
			throw new OrderNotFoundException("Unable to find the requested order");
		}
	}

}
