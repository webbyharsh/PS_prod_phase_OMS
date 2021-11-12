package com.oms.fill.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oms.fill.entities.client.Client;

//Provides crud operations
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

}
