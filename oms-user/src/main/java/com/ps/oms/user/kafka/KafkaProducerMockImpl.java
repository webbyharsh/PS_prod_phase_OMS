package com.ps.oms.user.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ps.oms.user.dto.UserUpdateRequest;
import com.ps.oms.user.entities.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KafkaProducerMockImpl implements IKafkaProducer {

	public void verificationEmailProducer(User newUser, String siteURL) throws JsonProcessingException {
		log.info("Mock verificationEmailProducer called");
	}

	public void resetPasswordEmailProducer(User user, String randomCode, String siteURL) throws JsonProcessingException {
		log.info("Mock resetPasswordEmailProducer called");
	}

	public void updateProfileChangeNotification(UserUpdateRequest newUser) throws JsonProcessingException {
		log.info("Mock updateProfileChangeNotification called");
	}
}
