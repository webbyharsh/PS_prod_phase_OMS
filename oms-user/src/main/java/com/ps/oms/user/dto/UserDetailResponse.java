package com.ps.oms.user.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.ps.oms.user.entities.Address;
import com.ps.oms.user.entities.Role;
import com.ps.oms.user.entities.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter @AllArgsConstructor
public class UserDetailResponse {
	
	private Long userId;
	private String name;
	private Integer age;
	private LocalDateTime createdAt;
	private LocalDateTime lastActiveAt;
	private Set<Role> roles;
	private String contact;
	private Address address;
	private String emailId;
	private boolean active;
	
	public UserDetailResponse(User user) {
		this.userId = user.getUserId();
		this.name = user.getName();
		this.age = user.getAge();
		this.createdAt = user.getCreatedAt();
		this.lastActiveAt = user.getLastActiveAt();
		this.roles = user.getRoles();
		this.contact = user.getContact();
		this.address = user.getAddress();
		this.emailId = user.getEmailId();
		this.active = user.isActive();
	}
}
