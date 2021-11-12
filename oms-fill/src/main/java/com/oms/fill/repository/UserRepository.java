package com.oms.fill.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oms.fill.entities.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}