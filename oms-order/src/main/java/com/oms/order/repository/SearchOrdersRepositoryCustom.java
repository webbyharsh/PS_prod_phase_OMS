package com.oms.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oms.order.dto.SearchOrdersRequest;
import com.oms.order.entities.Order;

public interface SearchOrdersRepositoryCustom {
	Page<Order> getSearchOrders(SearchOrdersRequest searchOrdersRequest,
			Long brokerId,
			Pageable pageable);
}
