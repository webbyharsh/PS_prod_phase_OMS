package com.oms.fill.entities.user;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "oms_user")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "name")
	private String name;

	@Email(message = "Email should be valid")
	@Column(name = "username")
	private String emailId;

	private boolean enabled;

	@Column(name = "verification_code", length = 64, unique = true)
	private String verificationCode;

	@Column(name = "password")
	private String password;

	@Column(name = "is_active")
	private boolean isActive;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "last_active_at")
	private LocalDateTime lastActiveAt;

	@Column(name = "contact")
	private String contact;

	@Type(type = "jsonb")
	@Column(name = "address", columnDefinition = "jsonb")
	private Address address;

	@Column(name = "age")
	private Integer age;

	// Remove either roleId or roles

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "oms_user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private Set<Role> roles = new HashSet<Role>();

	@PrePersist
	public void created() {
		this.createdAt = LocalDateTime.now();
		this.lastActiveAt = LocalDateTime.now();
		this.isActive = true;
	}

	public User(String name, @Email(message = "Email should be valid") String emailId, boolean enabled,
			String verificationCode, String password, boolean isActive, LocalDateTime createdAt,
			LocalDateTime lastActiveAt, String contact, Address address, Integer age) {
		super();
		this.name = name;
		this.emailId = emailId;
		this.enabled = enabled;
		this.verificationCode = verificationCode;
		this.password = password;
		this.isActive = isActive;
		this.createdAt = createdAt;
		this.lastActiveAt = lastActiveAt;
		this.contact = contact;
		this.address = address;
		this.age = age;

	}

}