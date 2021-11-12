package com.ps.oms.user.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ps.oms.user.entities.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
	
	Role findByName(String role);

}
