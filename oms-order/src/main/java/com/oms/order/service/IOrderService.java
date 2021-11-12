package com.oms.order.service;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oms.order.dto.OrderRequest;
import com.oms.order.entities.Order;
import com.oms.order.exceptions.OrderNotFoundException;

public interface IOrderService {

	public Order saveOrder(Order newOrder);

	public Page<Order> getOrdersByBrokerId(Long brokerId, Pageable pageable);

	// Order view service function
	public Order getOrderById(Long userId, Long orderId) throws OrderNotFoundException;
	public Order getOrderById(Long orderId) throws OrderNotFoundException;
	
	// Update order function
	public Order updateOrder(@Valid Order newOrderRequest, Long userId);

	Order getOrderAfterRequestedUpdates(Order orderToBeUpdated, OrderRequest updateOrderRequest);

	public void sendNotification(Order order);
}
