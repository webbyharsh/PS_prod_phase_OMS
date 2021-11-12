package com.oms.order.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oms.order.entities.Order;

// provides crud operations
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	
	// get order by order id
	Optional<Order> findById(Long id);
	
	// get Order list by user id
	@Query("select o from Order o where o.createdBy = :brokerId order by o.createdAt desc")
	Page<Order> getOrdersListByCreatedBy(@Param("brokerId") Long brokerId, Pageable pageable);

}