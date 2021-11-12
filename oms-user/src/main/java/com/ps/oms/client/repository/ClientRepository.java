package com.ps.oms.client.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ps.oms.client.entities.Client;

//Provides crud operations
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

	// verifies if email exists in the table
	boolean existsByEmailId(String emailId);

	
	@Query("SELECT c FROM Client c WHERE c.name LIKE ?1% ")
	public List<Client> search(String keyword);

}
