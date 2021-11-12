package com.oms.fill.services;

import static org.testng.Assert.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.oms.fill.dto.FillRequest;
import com.oms.fill.exceptions.BadRequestException;

@SpringBootTest
class FillValidatorTest extends AbstractTestNGSpringContextTests {

	// exception if request is null
	@Test
	void test_null_pointer_exception_if_fillRequest_null() {
		assertThrows(BadRequestException.class, () -> new FillValidator().validateOrderRequest(null));
	}

	// exception if quantity is negative
	@Test
	void test_exception_on_validate_fillRequest_if_quant_is_neg() {
		FillRequest testFillRequest = new FillRequest(188l, 1l, -30, "Amazon", new BigDecimal("100.0"),
				LocalDateTime.now());
		assertThrows(BadRequestException.class, () -> new FillValidator().validateOrderRequest(testFillRequest));
	}

}