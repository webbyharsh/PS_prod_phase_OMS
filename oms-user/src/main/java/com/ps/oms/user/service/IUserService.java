package com.ps.oms.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ps.oms.user.dto.ResetPasswordRequest;
import com.ps.oms.user.dto.UpdatePasswordRequest;
import com.ps.oms.user.dto.UserRequest;
import com.ps.oms.user.dto.UserUpdateRequest;
import com.ps.oms.user.entities.User;
import com.ps.oms.user.exceptions.PasswordUpdateException;
import com.ps.oms.user.exceptions.ResourceNotFoundException;
import com.ps.oms.user.exceptions.UpdateNotAllowedException;
import com.ps.oms.user.exceptions.UserException;

public interface IUserService {

	public boolean register(UserRequest userRequest, String siteURL)
			throws UserException, JsonProcessingException;

	public int verify(String verificationCode);

	public void updateUser(UserUpdateRequest newuser, Long userId)
			throws ResourceNotFoundException, UpdateNotAllowedException;

	public User getUserDetails(Long userId) throws ResourceNotFoundException;
	
	public boolean updateProfileChangeNotificationService(UserUpdateRequest newUser)  throws JsonProcessingException ;

	public void sendResetPasswordEmail(String email, String siteURL)
			throws UserException, JsonProcessingException;

	public void updatePassword(UpdatePasswordRequest upReq, Long userId) throws PasswordUpdateException;

	void resetPassword(ResetPasswordRequest rpReq) throws PasswordUpdateException;
	
	int resetPasswordTokenIsValid(String token);
	
	void deleteUser(String email);
	
	void deletePasswordToken(String emailId);

}
