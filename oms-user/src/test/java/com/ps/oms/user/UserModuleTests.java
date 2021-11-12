package com.ps.oms.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ps.oms.user.controller.UserController;
import com.ps.oms.user.kafka.IKafkaProducer;
import com.ps.oms.user.repository.ResetPasswordTokenRepository;
import com.ps.oms.user.repository.UserRepository;
import com.ps.oms.user.service.UserService;

@SpringBootTest
class UserModuleTests extends AbstractTestNGSpringContextTests {
	@Autowired
	private UserController userController;
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ResetPasswordTokenRepository resetPasswordRepository;
	@Autowired
	private IKafkaProducer producer;

	@Test
	void userControllerLoaded() {
		Assert.assertNotNull(userController);
	}

	@Test
	void userServiceLoaded() {
		Assert.assertNotNull(userService);
	}

	@Test
	void userRepositoryLoaded() {
		Assert.assertNotNull(userRepository);
	}

	@Test
	void userKafkaProducerLoaded() {
		Assert.assertNotNull(producer);
	}

	@Test
	void resetPasswordRepositoryLoaded() {
		Assert.assertNotNull(resetPasswordRepository);
	}
}