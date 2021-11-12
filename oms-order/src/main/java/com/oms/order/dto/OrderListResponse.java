package com.oms.order.dto;

import java.util.List;

import com.oms.order.entities.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderListResponse {

	private String message;
	private List<Order> orders;
	private Integer totalPages;
	private Long totalOrders;
	private Boolean isFirstPage;
	private Boolean isLastPage;
	private Integer pageNumber;
}