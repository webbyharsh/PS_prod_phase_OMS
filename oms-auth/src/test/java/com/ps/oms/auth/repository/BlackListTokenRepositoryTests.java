package com.ps.oms.auth.repository;

import com.ps.oms.auth.entities.BlackListToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BlackListTokenRepositoryTests extends AbstractTransactionalTestNGSpringContextTests {
             @Autowired
             BlackListTokenRepository blackListTokenRepository;
             @Test
             void testFindByJwtToken()
             {

                 BlackListToken blackListToken=new BlackListToken();
                 blackListToken.setJwtToken("tokenxyz");
                 blackListTokenRepository.save(blackListToken);
                Assert.assertEquals(blackListTokenRepository.findByJwtToken(blackListToken.getJwtToken()).getJwtToken(),blackListToken.getJwtToken());
             }

}
