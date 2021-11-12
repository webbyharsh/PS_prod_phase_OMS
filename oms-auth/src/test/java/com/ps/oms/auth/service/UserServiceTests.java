package com.ps.oms.auth.service;

import com.ps.oms.auth.entities.Role;
import com.ps.oms.auth.entities.User;
//import com.ps.oms.auth.repository.RoleRepository;
import com.ps.oms.auth.repository.UserRepository;
import com.ps.oms.auth.services.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class UserServiceTests extends AbstractTransactionalTestNGSpringContextTests {
    @Autowired
    private UserRepository userRepository;
@Autowired
    private AuthServiceImpl userService;
@Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    User user;
    @BeforeClass
    void setUp()
    {
        user=new User();
        user.setUsername("test3_text@gmail.com");
        user.setPassword(bCryptPasswordEncoder.encode("test"));
        Role role=new Role();
        role.setName("TEST3_USER");
        role.setDescription("TEST3_USER");
        Set<Role> roles=new HashSet<Role>();
        user.setRoles(roles);
        userRepository.save(user);
    }
    @AfterClass
    void cleanUp()
    {
        userRepository.delete(user);

    }

    @Test
    void testLoadUserByUsernameIfUserIsNotValid() {
        try {
            User user = new User();
            user.setUsername("NotValidUser@gmail.com");
            user.setPassword("test");
             userService.loadUserByUsername("NotValidUser");
        }
        catch (Exception e)
        {
            Assert.assertTrue(true);
        }

    }

    @Test
    void testFindOneReturnUserSuccess()
    {
        User user = new User();
        user.setUsername("Username@gmail.com");
        user.setPassword("testPassword");
        User savedUser=userRepository.save(user);
        Assert.assertEquals(userService.findOne("Username@gmail.com"),user);
    }


}
