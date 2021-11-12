package com.oms.order.entities.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

// address class to store as json in client entity table
@Getter @Setter @AllArgsConstructor
public class Address implements Serializable {
	private static final long serialVersionUID = 7702L;

	private String street;
	private String city;
	private String state;
	private String country;
	
}

