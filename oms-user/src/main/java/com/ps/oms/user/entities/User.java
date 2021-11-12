package com.ps.oms.user.entities;

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

import com.ps.oms.user.dto.UserRequest;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

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

	@Column(name = "verification_code", length = 64, unique=true)
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

	
	//Remove either roleId or roles
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "oms_user_role", 
			joinColumns = { @JoinColumn(name = "user_id") }, 
			inverseJoinColumns = { @JoinColumn(name = "role_id") })
	private Set<Role> roles = new HashSet<>();

	
	@PrePersist
	public void created() {
		this.createdAt = LocalDateTime.now();
		this.lastActiveAt = LocalDateTime.now();
		this.isActive = true;
	}

	public User(UserRequest userRequest) {
		this.emailId = userRequest.getEmailId();
		this.name = userRequest.getName();
		this.contact = userRequest.getContactNumber();
		this.age = userRequest.getAge();
		this.address = userRequest.getAddress();
	}
}