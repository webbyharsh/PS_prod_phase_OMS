package com.ps.oms.user.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ps.oms.user.dto.UserUpdateRequest;
import com.ps.oms.user.entities.User;
import com.ps.oms.user.kafka.dto.ProfileChangeMailContent;
import com.ps.oms.user.kafka.dto.ResetPasswordMailContent;
import com.ps.oms.user.kafka.dto.VerificationMailContent;

import lombok.extern.slf4j.Slf4j;

/*
 * The producer which uses Spring's KafkaTemplate 
 * to send the message to a topic named users, 
 */

@Slf4j
public class KafkaProducer implements IKafkaProducer {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	public void verificationEmailProducer(User newUser, String siteURL) throws JsonProcessingException {

		log.info("Sending Notification Message");
		VerificationMailContent vMailContent = new VerificationMailContent();

		vMailContent.setTemplate("verify-email-template-v1.flth");
		vMailContent.setName(newUser.getName());
		vMailContent.setEmailId(newUser.getEmailId());
		vMailContent.setVerificationCode(newUser.getVerificationCode());
		vMailContent.setSiteURL(siteURL);

		String vMailContentAsString = objectMapper.writeValueAsString(vMailContent);
		log.info("Send to Consumer : " + vMailContentAsString);

		String topic = "send-verification-mail";
		kafkaTemplate.send(topic, vMailContentAsString);

	}

	public void resetPasswordEmailProducer(User user, String randomCode, String siteURL)
			throws JsonProcessingException {

		log.info("Sending Notification Message");
		ResetPasswordMailContent rpMailContent = new ResetPasswordMailContent();

		rpMailContent.setTemplate("reset-password-email-template-v1.flth");
		rpMailContent.setName(user.getName());
		rpMailContent.setEmailId(user.getEmailId());
		rpMailContent.setResetPasswordCode(randomCode);
		rpMailContent.setSiteURL(siteURL);

		String rpMailContentAsString = objectMapper.writeValueAsString(rpMailContent);
		log.info("Send to Consumer : " + rpMailContentAsString);

		String topic = "send-reset-password-mail";
		kafkaTemplate.send(topic, rpMailContentAsString);

	}

	public void updateProfileChangeNotification(UserUpdateRequest newUser) throws JsonProcessingException {

		log.info("kafka producer calling, Sending profile change update");

		ProfileChangeMailContent profileChange = new ProfileChangeMailContent();

		profileChange.setUserId(newUser.getUserId());
		profileChange.setAddress((newUser.getAddress()).toString());
		log.info("profile change address is " + (newUser.getAddress()).toString());
		profileChange.setAge(newUser.getAge());
		profileChange.setContact(newUser.getContact());
		profileChange.setEmailId(newUser.getEmailId());
		profileChange.setName(newUser.getName());
		profileChange.setTemplate("update-profile-template-v1.flth");

		String profileChangeAsString = objectMapper.writeValueAsString(profileChange);

		log.info("Send to Consumer : " + profileChangeAsString);
		kafkaTemplate.send("profile-update-notification", profileChangeAsString);
		log.info("kafka producer called\n" + profileChangeAsString + "\n");

	}

}