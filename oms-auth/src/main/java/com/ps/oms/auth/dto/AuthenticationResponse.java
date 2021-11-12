package com.ps.oms.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private Long userId;
    private Boolean isAuthenticated;
    private String username;
    private Boolean isActive;
}
