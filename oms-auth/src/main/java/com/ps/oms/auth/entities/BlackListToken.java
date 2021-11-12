package com.ps.oms.auth.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@Entity
@NoArgsConstructor
public class BlackListToken {

    @Id
    private String jwtToken;
    private Date expirationTime;

}
