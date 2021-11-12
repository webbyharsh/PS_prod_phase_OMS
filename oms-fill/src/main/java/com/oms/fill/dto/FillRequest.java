package com.oms.fill.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FillRequest {

	@NotNull
	private Long orderId;

	@NotNull
	private Long exchangeId;

	@NotNull
	private Integer quantity;

	@NotNull
	private String stock;

	@NotNull
	private BigDecimal executedPrice;
	@NotNull
	private LocalDateTime executedTime;

}