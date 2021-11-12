package com.oms.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oms.order.entities.client.Client;

public interface ClientRepository  extends JpaRepository<Client, Long> {

}
