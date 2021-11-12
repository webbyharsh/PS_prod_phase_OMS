package com.oms.order.dto;

import java.time.LocalDate;

import com.oms.order.entities.Order.Type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchOrdersRequest {
	
	private String stock;
	private Type type;
	private LocalDate startDate;
	private LocalDate endDate;
	private String clientName;
	private String clientEmail;
	
}
