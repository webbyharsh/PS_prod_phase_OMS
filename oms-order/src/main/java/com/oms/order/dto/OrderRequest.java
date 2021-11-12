package com.oms.order.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

	@NotNull
	private Long clientId;

	@NotNull
	private Integer quantity;

	@NotNull
	private String stock;

	@NotNull
	private String side;

	@NotNull
	private String type;

	private BigDecimal targetPrice;
	
	private Long orderId = null;	
	
}
