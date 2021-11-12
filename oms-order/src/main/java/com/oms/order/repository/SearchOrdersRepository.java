package com.oms.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oms.order.entities.Order;

public interface SearchOrdersRepository extends JpaRepository<Order, Long>, SearchOrdersRepositoryCustom {

}
