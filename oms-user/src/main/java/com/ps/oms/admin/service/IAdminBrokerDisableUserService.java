package com.ps.oms.admin.service;

import java.util.List;

import com.ps.oms.admin.entities.AdminBrokerDisableUser;
import com.ps.oms.admin.exception.ResourceNotFoundException;
import com.ps.oms.admin.exception.UpdateNotAllowedException;

public interface IAdminBrokerDisableUserService {

	public  List<AdminBrokerDisableUser> getAllBroker();
	public void setStatusUpdate(Integer userId, Boolean isActive) throws ResourceNotFoundException,UpdateNotAllowedException;
}
