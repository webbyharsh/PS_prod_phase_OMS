package com.oms.order.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oms.order.dto.SearchOrdersRequest;
import com.oms.order.entities.Order;
import com.oms.order.exceptions.OrderNotFoundException;

public interface ISearchOrderService {
	public Page<Order> getSearchOrders(SearchOrdersRequest searchOrdersRequest, 
			Long brokerId,
			Pageable pageable) throws OrderNotFoundException;
}
