package com.ps.oms.user.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ps.oms.user.dto.UserUpdateRequest;
import com.ps.oms.user.entities.User;

public interface IKafkaProducer {

	public void verificationEmailProducer(User newUser, String siteURL) throws JsonProcessingException;

	public void resetPasswordEmailProducer(User user, String randomCode, String siteURL) throws JsonProcessingException;

	public void updateProfileChangeNotification(UserUpdateRequest newUser) throws JsonProcessingException;
}
