package com.oms.fill.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderNotification {
	private String template;
	private String name;
	private String email;
	private String status;
	private Long orderId;
	private String timestamp;
}