package com.ps.oms.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

@SpringBootTest
public class RestAccessDeniedHandlerTests extends AbstractTestNGSpringContextTests {
   @Autowired
   RestAccessDeniedHandler restAccessDeniedHandler;
    @Test
    void TestHandleMethod() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response= new MockHttpServletResponse();
        AccessDeniedException accessDeniedException = null;
        try {
            restAccessDeniedHandler.handle(request,response, accessDeniedException);
            Assert.assertTrue(true);
        }
        catch (Exception e)
        {
            Assert.assertTrue(false);
        }


    }
}
