package com.oms.fill.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oms.fill.entities.Order;

// provides crud operations
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}