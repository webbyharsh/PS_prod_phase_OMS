package com.ps.oms.auth.repository;

import com.ps.oms.auth.entities.RefreshAccessToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface RefreshAccessTokenRepository extends CrudRepository<RefreshAccessToken, String> {
    RefreshAccessToken findByRefreshToken(String refreshToken);

    @Modifying
    @Transactional
    public void deleteByRefreshTokenExpirationDateBefore(Date expirationDate);
}
