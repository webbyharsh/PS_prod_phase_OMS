package com.oms.order.dto;

import java.math.BigDecimal;

import com.oms.order.entities.Order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderResponse {

	private Long clientId;

	private Long orderId;

	private Long createdBy;

	private Integer quantity;

	private String stock;

	private String side;

	private String type;

	private BigDecimal targetPrice;

	public OrderResponse(Order savedOrder) {
		this.orderId = savedOrder.getOrderId();
		this.clientId = savedOrder.getClientId();
		this.createdBy = savedOrder.getCreatedBy();
		this.quantity = savedOrder.getQuantity();
		this.stock = savedOrder.getStock();
		this.side = savedOrder.getSide().toString();
		this.type = savedOrder.getType().toString();
		this.targetPrice = savedOrder.getTargetPrice();
	}

}
