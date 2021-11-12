package com.oms.fill.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oms.fill.dto.FillRequest;
import com.oms.fill.exceptions.BadRequestException;
import com.oms.fill.exceptions.OrderNotFoundException;
import com.oms.fill.services.FillValidator;
import com.oms.fill.services.IFillService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/v1/fill")
public class FillController {

	@Autowired
	private IFillService fillService;
	@Autowired
	private FillValidator fillValidator;

	@PostMapping("/response")
	public ResponseEntity<String> fillOrder(@Valid @RequestBody FillRequest newfillRequest)
			throws OrderNotFoundException, BadRequestException {

		if (newfillRequest != null) {
			log.info("Request in Fill Order");
			fillValidator.validateOrderRequest(newfillRequest);
			fillService.fillOrder(newfillRequest);

			// returns the saved order and status created
			return new ResponseEntity<String>("Filled", HttpStatus.OK);
		} else {

			log.error("Fill request in Null");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		}

	}

}