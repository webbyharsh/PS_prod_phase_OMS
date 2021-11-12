package com.ps.oms.client.entities;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "oms_client", uniqueConstraints = { @UniqueConstraint(columnNames = { "email_id" }) })
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@NoArgsConstructor @Getter @Setter 
public class Client {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "client_id")
	private Long clientId;

	@NotNull(message = "Client Name cannot be Null")
	@Column(name = "name")
	private String name;
	
	@Email
	@NotNull(message = "Client Email cannot be Null")
	@Column(name = "email_id")
	private String emailId;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@NotNull
	@Column(name = "created_by")
	private Long createdBy;
	
	@Column(name = "last_modified_at")
	private LocalDateTime lastModifiedAt;
	
	@Column(name = "last_modified_by")
	private Long lastModifiedBy;

	@NotNull
	@Type(type = "jsonb")
	@Column(name = "address", columnDefinition = "jsonb")
	private Address address;

	@NotNull
	@Pattern(regexp="(^$|[0-9]{10})")
	@Column(name = "contact")
	private String contact;
	
	@Column(name = "is_active")
	private Boolean isActive;


	public Client(String clientName, String clientEmail, String contactNumber, Address address, Long userId) {
		this.name = clientName;
		this.emailId = clientEmail;
		this.address = address;
		this.contact = contactNumber;
		this.createdBy = userId;
	}


	@PrePersist
	public void onCreation() {
		this.createdAt = LocalDateTime.now();
		this.lastModifiedAt = createdAt;
		this.lastModifiedBy = createdBy;
		this.isActive = true;
	}
}
