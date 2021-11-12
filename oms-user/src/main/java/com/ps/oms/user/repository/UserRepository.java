package com.ps.oms.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ps.oms.user.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	// get user by userId
	Optional<User> findById(Long id);
	
	User findByUserId(Long id);
	
	User findByEmailIdIgnoreCase(String email);

	@Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
	User findByVerificationCode(String code);

	// verifies if email exists in the table
	boolean existsByEmailIdIgnoreCase(String emailId);
	
	void deleteByEmailId(String email);
}
