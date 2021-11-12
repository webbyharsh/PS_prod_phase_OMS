package com.oms.order.service;

import org.springframework.stereotype.Service;

import com.oms.order.dto.OrderRequest;
import com.oms.order.exceptions.BadRequestException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderValidator {

	// validation checks
	public Boolean validateOrderRequest(OrderRequest newOrderRequest) throws BadRequestException {

		log.info("Validating Order Request");

		if (newOrderRequest == null) {
			throw new BadRequestException("Request is Empty or Incorrect");
		} else if (newOrderRequest.getClientId() == null || newOrderRequest.getQuantity() == null
				|| newOrderRequest.getStock() == null || newOrderRequest.getSide() == null
				|| newOrderRequest.getType() == null) {
			throw new BadRequestException("One or more Order fields missing");
		} else if (!(newOrderRequest.getType().equalsIgnoreCase("MARKET")
				|| newOrderRequest.getType().equalsIgnoreCase("LIMIT"))) {
			throw new BadRequestException("Type must be MARKET or LIMIT");
		} else if (!(newOrderRequest.getSide().equalsIgnoreCase("BUY")
				|| newOrderRequest.getSide().equalsIgnoreCase("SELL"))) {
			throw new BadRequestException("Side must be BUY or SELL");
		} else if (newOrderRequest.getType().equalsIgnoreCase("MARKET") && newOrderRequest.getTargetPrice() != null) {
			throw new BadRequestException("Target price has to be empty for Market type");
		} else if (newOrderRequest.getType().equalsIgnoreCase("LIMIT") && newOrderRequest.getTargetPrice() == null) {
			throw new BadRequestException("Target price cannot be empty for Limit type");
		} else if (newOrderRequest.getQuantity() < 1) {
			throw new BadRequestException("Quantity must be positive");
		} else {
			return true;
		}
	}
}
