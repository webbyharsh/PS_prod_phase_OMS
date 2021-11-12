package com.ps.oms.auth.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshAccessToken {
    @Id
    private String refreshToken;
    private String accessToken;
    private Date refreshTokenExpirationDate;
}
