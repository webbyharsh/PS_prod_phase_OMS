package com.ps.oms.user.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ps.oms.user.dto.ResetPasswordRequest;
import com.ps.oms.user.dto.UpdatePasswordRequest;
import com.ps.oms.user.dto.UserRequest;
import com.ps.oms.user.dto.UserUpdateRequest;
import com.ps.oms.user.entities.ResetPasswordToken;
import com.ps.oms.user.entities.Role;
import com.ps.oms.user.entities.User;
import com.ps.oms.user.exceptions.PasswordUpdateException;
import com.ps.oms.user.exceptions.ResourceNotFoundException;
import com.ps.oms.user.exceptions.UpdateNotAllowedException;
import com.ps.oms.user.exceptions.UserException;
import com.ps.oms.user.kafka.IKafkaProducer;
import com.ps.oms.user.repository.ResetPasswordTokenRepository;
import com.ps.oms.user.repository.RoleRepository;
import com.ps.oms.user.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;

@Service
@Slf4j
public class UserService implements IUserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ResetPasswordTokenRepository resetPasswordRepository;
	
	@Autowired
	RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private IKafkaProducer producer;
	
	@Transactional
	public void deleteUser(String emailId) {
		
		userRepository.deleteByEmailId(emailId);
	}
	
	@Transactional
	public void deletePasswordToken(String emailId) {
		
		User user = userRepository.findByEmailIdIgnoreCase(emailId);
		resetPasswordRepository.deleteByUser(user);
	}
	
	
	public boolean tokenValid(User user) {
		LocalDateTime validTill = user.getCreatedAt().plusDays(1);
		return LocalDateTime.now().isBefore(validTill);
	}
	
	
	

	public boolean register(UserRequest userRequest, String siteURL)
			throws UserException, JsonProcessingException {

		log.info("Register Method in Service Called");

		boolean exists = userRepository.existsByEmailIdIgnoreCase(userRequest.getEmailId());
		if (exists) {
			throw new UserException("Email already registered or verification pending");
		}

		User newUser = new User(userRequest);
		
		String randomCode = RandomString.make(64);
		newUser.setVerificationCode(randomCode);
		
		Role userRole = roleRepository.findByName("User");
		newUser.getRoles().add(userRole);
		
		newUser.setEnabled(false);

		newUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));

		userRepository.save(newUser);

		log.info("Forwarding to Kafka To Send Verification Mail");

		// Kafka producer
		producer.verificationEmailProducer(newUser, siteURL);

		return true;

	}

	@Transactional
	public int verify(String verificationCode) {
		log.info("Verifying and Fetching User by VerificationCode : " + verificationCode);

		User user = userRepository.findByVerificationCode(verificationCode);

		if (user == null) {
			return -1;
			//throw new VerificationFailedException("User already verified or user doesn't exist");
		} else if (!tokenValid(user)) {
			userRepository.deleteByEmailId(user.getEmailId());
			return -1;
			
			//throw new VerificationFailedException("Your verification token has expired");
		}

		log.info("User Verified, Updating Data in Table");

		//user.setVerificationCode(null);
		user.setEnabled(true);
		userRepository.save(user);

		return 1;
	}


	//Update password method
	public void updatePassword(UpdatePasswordRequest updatePasswordRequest, Long userId)
			throws PasswordUpdateException {

		log.info("updatePassword of service called");
		User user = userRepository.findByUserId(userId);
		
		log.info("Fetched User based on userId");

		String oldPassword = updatePasswordRequest.getOldPassword();
		String newPassword = updatePasswordRequest.getNewPassword();
		String confirmPassword = updatePasswordRequest.getConfirmPassword();
		
		if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
			throw new PasswordUpdateException("The old password is incorrect");
		} else if (newPassword.equals(oldPassword)) {
			throw new PasswordUpdateException("New and Old Password cannot be same");
		} else if (!newPassword.equals(confirmPassword)) {
			throw new PasswordUpdateException("New and Confirm Password do not match");
		}

		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);

	}
	
	
	public int resetPasswordTokenIsValid(String token) {
		
		log.info("resetPasswordTokenIsValid of service called");
		ResetPasswordToken rpToken = resetPasswordRepository.findByToken(token);
		
		log.info("Fetched ResetPasswordToken based on token value");
		
		if (rpToken == null) {
			return -1;
		} else if (rpToken.getExpiryDate().isBefore(LocalDateTime.now())) {
			resetPasswordRepository.deleteByToken(token);
			return 0;
		}
		
		return 1;
	}
	
	
	//Reset password method
	@Transactional
	public void resetPassword(ResetPasswordRequest resetPasswordRequest)
			throws PasswordUpdateException {
		
		log.info("resetPassword of service called");
		
		String token = resetPasswordRequest.getToken();
		String newPassword = resetPasswordRequest.getNewPassword();
		String confirmPassword = resetPasswordRequest.getConfirmPassword();

		if (!newPassword.equals(confirmPassword)) {
			throw new PasswordUpdateException("New and Confirm Password do not match");
		}
		
		ResetPasswordToken rpToken = resetPasswordRepository.findByToken(token);
		
		log.info("Fetched ResetPasswordToken based on token value");
		
		User user = rpToken.getUser();
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
		resetPasswordRepository.deleteByToken(token);
		
		log.info("Reset Password and deleted token");

	}
	

	//Send reset password email method
	public void sendResetPasswordEmail(String email, String siteURL)
			throws UserException, JsonProcessingException {
		
		log.info("sendResetPasswordEmail of service called");
		
		User user = userRepository.findByEmailIdIgnoreCase(email);
		log.info("Fetched user based on email : " + email);
		
		if (user == null) {
			throw new UserException("The provided emailId is not registered with us.");
		} else if (!user.isEnabled()) {
			throw new UserException("The associated user is not enabled.");
		}

		String randomCode = RandomString.make(64);
		ResetPasswordToken rpToken = new ResetPasswordToken(user, randomCode);

		resetPasswordRepository.save(rpToken);

		producer.resetPasswordEmailProducer(user, randomCode, siteURL);
		
		log.info("ResetPasswordToken saved and email producer called");

	}
	
	
	
	
	
	

	@Override
	public User getUserDetails(Long userId) throws ResourceNotFoundException {

		log.info("UserService");
		User userDetails = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
		userDetails.setPassword("XXXX");
		log.info(String.format("%s user found", userDetails));
		return userDetails;
	}

	@Override
	public void updateUser(UserUpdateRequest newuser, Long userId)
			throws ResourceNotFoundException, UpdateNotAllowedException {

		log.info("UserService");
		User user = null;

		user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
		log.info(String.format("%s to be updated", user));
		log.info("check if update allowed");

		if (user.getEmailId().equals(newuser.getEmailId()) == false)
			throw new UpdateNotAllowedException("email", "User", "id", userId);
		if (user.getUserId().equals(newuser.getUserId()) == false)
			throw new UpdateNotAllowedException("userId and roleId", "User", "id", userId);

		log.info("update allowed");
		user.setName(newuser.getName());
		user.setAge(newuser.getAge());
		user.setContact(newuser.getContact());
		user.setAddress(newuser.getAddress());
		userRepository.save(user);
		log.info(String.format("%s updated in database", newuser));
	}
	
	
	@Override
	public boolean updateProfileChangeNotificationService(UserUpdateRequest newUser) throws JsonProcessingException {
		//String siteURL=
		log.info("calling updateProfileChangeNotificationService  ");
		producer.updateProfileChangeNotification(newUser);
		log.info("called updateProfileChangeNotificationService ");
		return true;
	}
}
