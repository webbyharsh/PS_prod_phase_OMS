package com.ps.oms.user.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "oms_password_reset_token")
@Getter @Setter @NoArgsConstructor
public class ResetPasswordToken {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long tokenId;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(name = "token", length = 64, unique=true)
    private String token;
    
    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;
    
	@PrePersist
	public void created() {
		this.expiryDate = LocalDateTime.now().plusHours(2);
	}
	
	public ResetPasswordToken(User user, String token) {
		this.user = user;
		this.token = token;
	}
    
}