package com.ps.oms.user.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.transaction.annotation.Transactional;

//import java.util.List;

import org.testng.annotations.Test;

import com.ps.oms.user.dto.UserRequest;
import com.ps.oms.user.entities.Address;
import com.ps.oms.user.entities.User;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
class UserRepositoryTests extends AbstractTransactionalTestNGSpringContextTests{

	@Autowired
	private UserRepository userRepository;
	
	// test for read client to table
	@Test
	@Rollback(true)
	@Transactional
	void test_read_user() {

		Address address = new Address("Test Street", "Test City", "Test State", "Test Country");
		User testUser = new User(new UserRequest("Sounak", "test@gmail.com", "testNAME@123", "1234567890", address, 25));

		User savedUser = userRepository.save(testUser);

		Optional<User> returnedUser = userRepository.findById(savedUser.getUserId());

		assertTrue(returnedUser.isPresent());
	}
	
	// test for saving user to table
	@Test(enabled=false)
	@Rollback(true)
	@Transactional
	void test_create_user() {
		Address address = new Address("Test Street", "Test City", "Test State", "Test Country");
		User testUser = new User(new UserRequest("Test Name", "test@name.com", "testNAME@123", "1234567890", address, 25));

		User savedUser = userRepository.save(testUser);
		
		String expectedName = "Test Name";
		String expectedEmailId = "test@name.com";
		String expectedContact = "1234567890";
		Integer expectedAge = 25;

		assertEquals(savedUser.getAge(), expectedAge);
		assertEquals(savedUser.getName(), expectedName);
		assertEquals(savedUser.getEmailId(), expectedEmailId);
		assertEquals(savedUser.getContact(), expectedContact);
		assertThat(address).usingRecursiveComparison().isEqualTo(savedUser.getAddress());
		
	}

	// test for getting all users from the table
	@Test(enabled=false)
	@Rollback(true)
	@Transactional
	void test_list_users() {
		Address address = new Address("Test Street", "Test City", "Test State", "Test Country");
		User testUser = new User(new UserRequest("Test Name", "test@name.com", "testNAME@123", "1234567890", address, 25));

		userRepository.save(testUser);

		List<User> users = (List<User>) userRepository.findAll();
		assertThat(users).size().isPositive();
	}

	// test for deleting users from table
	@Test(enabled=false)
	@Rollback(true)
	@Transactional
	void test_delete_users() {
		Address address = new Address("Test Street", "Test City", "Test State", "Test Country");
		User testUser = new User(new UserRequest("Test Name", "test@name.com", "testNAME@123", "1234567890", address, 25));

		User savedUser = userRepository.save(testUser);

		Long countBefore = userRepository.count();
		
		userRepository.deleteById(savedUser.getUserId());
		Long countAfter = userRepository.count();
		
		assertEquals(countBefore - countAfter, 1);

	}
}
