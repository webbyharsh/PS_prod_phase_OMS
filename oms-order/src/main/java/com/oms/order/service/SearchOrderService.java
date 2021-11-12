package com.oms.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.oms.order.dto.SearchOrdersRequest;
import com.oms.order.entities.Order;
import com.oms.order.exceptions.OrderNotFoundException;
import com.oms.order.repository.SearchOrdersRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SearchOrderService implements ISearchOrderService {
	
	@Autowired
	SearchOrdersRepository searchOrderRepository;
	
	public Page<Order> getSearchOrders(SearchOrdersRequest searchOrdersRequest, 
			Long brokerId,
			Pageable pageable) throws OrderNotFoundException {
		log.info("Stocks search results");
		return searchOrderRepository.getSearchOrders(searchOrdersRequest, brokerId, pageable);
	}
}

