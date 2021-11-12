package com.ps.oms.admin.controller;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ps.oms.admin.dto.BrokerListServiceResponse;
import com.ps.oms.admin.dto.BrokerStatusServiceRequest;
import com.ps.oms.admin.dto.BrokerStatusServiceResponse;
import com.ps.oms.admin.entities.AdminBrokerDisableUser;
import com.ps.oms.admin.exception.ResourceNotFoundException;
import com.ps.oms.admin.exception.UpdateNotAllowedException;
import com.ps.oms.admin.service.IAdminBrokerDisableUserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/broker")
@Slf4j
public class AdminBrokerDisableUserController {
	
	@Autowired
	private IAdminBrokerDisableUserService brokerService;
	
	@ApiOperation("Get Broker List")		
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code=400, message="BAD REQUEST"),
			@ApiResponse(code=401, message="UNAUTHORIZED"),
			@ApiResponse(code = 404, message = "NOT FOUND"),
			@ApiResponse(code = 403, message = "FORBIDDEN"),
			@ApiResponse(code = 500, message = "SERVER ERROR")})
	@GetMapping("/getList")
	public ResponseEntity<List<AdminBrokerDisableUser>> getAllBrokerList(@RequestHeader("Authorization") String jwt)
	{
		
		BrokerListServiceResponse brokerServiceResponse= new BrokerListServiceResponse();
		if (!jwt.isEmpty()) {
		List<AdminBrokerDisableUser> listBroker=null;
		

		HttpStatus httpStatus = null;
		try {
			listBroker = brokerService.getAllBroker();
			
			Collections.reverse(listBroker);
			if(!listBroker.isEmpty()) {
				log.info("Successfully retrived");
				httpStatus = HttpStatus.OK;
			} else {
				log.info("Empty List");
				httpStatus = HttpStatus.NOT_FOUND;
			}
			
			
			brokerServiceResponse.setResponse(listBroker);
			
			return ResponseEntity.status(httpStatus).body(brokerServiceResponse.getResponse());
			} 
		 catch(Exception e)
		 {
			log.error("500 internal server error");

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(brokerServiceResponse.getResponse());
		 	}
		}
		else {
			log.error("400 Bad Request");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(brokerServiceResponse.getResponse());
		}
	 	
       
	}
	
	
	@ApiOperation("Update Broker status ")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code=400, message="BAD REQUEST"),
			@ApiResponse(code=401, message="UNAUTHORIZED"),
			@ApiResponse(code = 404, message = "NOT FOUND"),
			@ApiResponse(code = 403, message = "FORBIDDEN"),
			@ApiResponse(code = 500, message = "SERVER ERROR")})
	@PutMapping("/updateStatus")
	public ResponseEntity<String >updateStatus(@RequestBody @Valid BrokerStatusServiceRequest serviceRequest ,@RequestHeader("Authorization") String jwt)
	throws Exception
	{
		try {
				Integer userId=Integer.parseInt(serviceRequest.getUserId());
				String status= serviceRequest.getIsActive();
				if(status.equals("true") || status.equals("false"))
				{
					
					Boolean active=Boolean.parseBoolean(status);
					BrokerStatusServiceResponse brokerStatusServiceResponse= new BrokerStatusServiceResponse ();	
					if (!jwt.isEmpty()) {

						try {
			
							brokerService.setStatusUpdate(userId,active);
							brokerStatusServiceResponse.setResponseMessage("Success");
							log.info("user active status updated");
			
						}
						catch(ResourceNotFoundException rnfe) {
							log.error("404  No detail found for given user");
							brokerStatusServiceResponse.setResponseMessage(rnfe.getMessage());
							return ResponseEntity.status(HttpStatus.NOT_FOUND).body(brokerStatusServiceResponse.getResponseMessage());   
						}
						catch(UpdateNotAllowedException unae) {
							log.error("403 update not allowed");
							
							brokerStatusServiceResponse.setResponseMessage(unae.getMessage());
							return ResponseEntity.status(HttpStatus.FORBIDDEN).body(brokerStatusServiceResponse.getResponseMessage());
						}
						return ResponseEntity.status(HttpStatus.OK).body(brokerStatusServiceResponse.getResponseMessage());
		
					}else {
						log.error("401 unauthorized request");
						return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(brokerStatusServiceResponse.getResponseMessage());
					}
				}
				else {
					throw new Exception() ;
				}
		}
		catch(Exception e)
		{
			log.error("400 Bad Request exception");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	
	}
}
	
