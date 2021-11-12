package com.oms.order.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Component
@ToString
public class AuthConfig {
    private String username;
    private List<String> roles;
    private Long userId;
    private Boolean isActive;
}
