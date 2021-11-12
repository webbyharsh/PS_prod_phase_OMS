package com.oms.fill.services;

import org.springframework.stereotype.Service;

import com.oms.fill.dto.FillRequest;
import com.oms.fill.exceptions.BadRequestException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FillValidator {

	// validation checks
	public Boolean validateOrderRequest(FillRequest fillRequest) throws BadRequestException {

		log.info("Validating Fill Request");

		if (fillRequest == null) {
			throw new BadRequestException("Request is Empty or Incorrect");
		} else if (fillRequest.getQuantity() < 1) {
			throw new BadRequestException("Quantity must be positive");
		} else {
			return true;
		}
	}
}