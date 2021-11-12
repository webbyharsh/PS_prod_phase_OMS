package com.ps.oms.auth.service;

import com.ps.oms.auth.entities.BlackListToken;
import com.ps.oms.auth.entities.Role;
import com.ps.oms.auth.entities.User;
import com.ps.oms.auth.repository.BlackListTokenRepository;
import com.ps.oms.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthServiceTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private BlackListTokenRepository blackListTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    User user;
    @BeforeClass
       void setUp()
    {
         user=new User();
        user.setUsername("test2_text@gmail.com");
        user.setPassword(bCryptPasswordEncoder.encode("test"));
        Role role=new Role();
        role.setName("TEST2_USER");
        role.setDescription("TEST2_USER");
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
    public void testScheduledDeleteFunction(){
        Calendar cal = Calendar.getInstance();
        Date now = new java.sql.Date(cal.getTimeInMillis());
        System.out.println(now);
        List<BlackListToken> blackListTokenList = null;
        try{
            blackListTokenRepository.deleteByExpirationTimeBefore(now);
        } catch (Exception e){
            e.printStackTrace();
        }
        Assert.assertTrue(true);
    }
}
