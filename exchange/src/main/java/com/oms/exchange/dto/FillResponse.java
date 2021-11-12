package com.oms.exchange.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FillResponse {
	
	private Long orderId;
	
	private Long exchangeId;
	
	private Integer quantity;
	
	private BigDecimal executedPrice;
	
	private LocalDateTime executedTime;
	
	private String stock;
}
