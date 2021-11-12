package com.ps.oms.user.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ps.oms.user.entities.ResetPasswordToken;
import com.ps.oms.user.entities.User;

@Repository
public interface ResetPasswordTokenRepository extends CrudRepository<ResetPasswordToken, Long> {
	
	ResetPasswordToken findByToken(String token);
	
	boolean existsByToken(String token);
	
	Long deleteByToken(String token);
	
	void deleteByUser(User user);
}
