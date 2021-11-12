package com.oms.fill.entities.client;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.oms.fill.entities.user.Address;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "oms_client")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
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
	@Pattern(regexp = "(^$|[0-9]{10})")
	@Column(name = "contact")
	private String contact;

	@Column(name = "is_active")
	private Boolean isActive;

	@PrePersist
	public void onCreation() {
		this.createdAt = LocalDateTime.now();
		this.lastModifiedAt = createdAt;
		this.lastModifiedBy = createdBy;
		this.isActive = true;
	}

	public Client(@NotNull(message = "Client Name cannot be Null") String name,
			@Email @NotNull(message = "Client Email cannot be Null") String emailId, LocalDateTime createdAt,
			@NotNull Long createdBy, LocalDateTime lastModifiedAt, Long lastModifiedBy, @NotNull Address address,
			@NotNull @Pattern(regexp = "(^$|[0-9]{10})") String contact, Boolean isActive) {
		super();
		this.name = name;
		this.emailId = emailId;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.lastModifiedAt = lastModifiedAt;
		this.lastModifiedBy = lastModifiedBy;
		this.address = address;
		this.contact = contact;
		this.isActive = isActive;
	}
}