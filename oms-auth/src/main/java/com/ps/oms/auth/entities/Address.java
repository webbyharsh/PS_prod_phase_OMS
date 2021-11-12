package com.ps.oms.auth.entities;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// address class to store as json in client entity table
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Address implements Serializable {
    private static final long serialVersionUID = 7702L;

    private String street;
    private String city;
    private String state;
    private String country;
}