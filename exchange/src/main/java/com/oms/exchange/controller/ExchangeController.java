package com.oms.exchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oms.exchange.dto.OrderRequest;
import com.oms.exchange.dto.OrderResponse;
import com.oms.exchange.service.IExchangeService;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class ExchangeController {

	@Autowired
	private IExchangeService exchangeService;

	@PostMapping("/exchange/order")
	public ResponseEntity<String> mockExch(@Validated @RequestBody OrderRequest newOrderRequest)
			throws Exception {

		if (newOrderRequest != null) {

			// calls Exchange service to validate the order
			log.info("New order request Submitted By User");
			exchangeService.exchangeService(newOrderRequest);

			log.info("executing return ");

			return new ResponseEntity<String>( "RECEIVED",HttpStatus.OK);

		} else {
			log.error("Order request in Null");
			return new ResponseEntity<String>("RECEIVED NULL ORDER",HttpStatus.BAD_REQUEST);
		}

	}
}
