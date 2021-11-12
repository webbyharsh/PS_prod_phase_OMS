package com.ps.oms.admin.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ps.oms.admin.entities.AdminBrokerDisableUser;

@Repository
public interface AdminBrokerDisableUserRepository extends CrudRepository<AdminBrokerDisableUser, Integer> {

}
