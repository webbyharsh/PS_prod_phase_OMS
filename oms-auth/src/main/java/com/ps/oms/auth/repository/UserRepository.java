package com.ps.oms.auth.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ps.oms.auth.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	User findByUsername(String username);

	@Query("select o.userId from User o where o.username = :username")
	Long findUserIdFromUserName(@Param(value="username") String username);

}