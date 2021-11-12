package com.ps.oms.auth.repository;

import com.ps.oms.auth.entities.BlackListToken;
import org.springframework.data.jpa.repository.Modifying;

import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Repository
public interface BlackListTokenRepository extends CrudRepository<BlackListToken, String> {

    BlackListToken findByJwtToken(String token);

    @Modifying
    @Transactional
    public void deleteByExpirationTimeBefore(Date time);

}
