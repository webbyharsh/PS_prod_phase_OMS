package com.ps.oms.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ps.oms.admin.entities.AdminBrokerDisableUser;
import com.ps.oms.admin.exception.ResourceNotFoundException;
import com.ps.oms.admin.exception.UpdateNotAllowedException;
import com.ps.oms.admin.repository.AdminBrokerDisableUserRepository;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class AdminBrokerDisableUserService implements IAdminBrokerDisableUserService{
	
	@Autowired
	private AdminBrokerDisableUserRepository userRepository;
	
	
	public List<AdminBrokerDisableUser> getAllBroker()
	{
		try {
		List<AdminBrokerDisableUser> brokerList=new ArrayList<>();
		userRepository.findAll()
		.forEach(brokerList::add);
		
		 return brokerList; 
		}catch(Exception e)
		{
			log.info("Brokers not found in database");
			throw e;
		}
	}
	public void setStatusUpdate(Integer userId, Boolean isActive) throws ResourceNotFoundException,UpdateNotAllowedException
	{
		log.info("UserService");
		AdminBrokerDisableUser  userUpdate = null;
		try {
				userUpdate = userRepository.findById(userId).orElseThrow(() -> new
				ResourceNotFoundException("User", "id", userId));
		
				if(userUpdate.getUserId().equals(userId))
				{
					log.info("User is verfied");
					if(Boolean.compare(userUpdate.isActive(),isActive) !=0)
					{
						log.info("Status of user %s is updated",userId);
						userUpdate.setActive(isActive);
						userRepository.save(userUpdate);
						log.info(String.format("%s updated in database", userId));
				
					}
					else
					{
						log.info("Update to be made is same as previous status ");
						throw new UpdateNotAllowedException(userId,isActive);
					}
		}
		else {
			log.info("Resource Not Found ");
				throw new ResourceNotFoundException("User", "id", userId);
			}
		
		}
		catch(Exception err){
			log.info("internal server error");
			throw  err;
		}
			
		}
	}



