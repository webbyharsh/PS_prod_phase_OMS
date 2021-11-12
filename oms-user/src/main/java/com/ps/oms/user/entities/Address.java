package com.ps.oms.user.entities;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// address class to store as json in client entity table
@Getter @Setter @AllArgsConstructor
public class Address implements Serializable {
	private static final long serialVersionUID = 7702L;

	private String street;
	private String city;
	private String state;
	private String country;
	@Override
	public String toString()
    {
        return  street + " , " + city + " , " + state + " , " + country;
    }
	
}

