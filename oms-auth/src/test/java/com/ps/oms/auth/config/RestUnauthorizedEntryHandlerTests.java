package com.ps.oms.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SpringBootTest
public class RestUnauthorizedEntryHandlerTests extends AbstractTestNGSpringContextTests {
   
    @Autowired
    RestUnauthorizedEntryHandler restUnauthorizedEntryHandler;
   @Test
    void TestCommenceMethod() throws IOException {
       MockHttpServletRequest request = new MockHttpServletRequest();
       MockHttpServletResponse response= new MockHttpServletResponse();
       AuthenticationException authException = null;
       try {
           restUnauthorizedEntryHandler.commence(request, response,authException) ;
           Assert.assertTrue(true);
       }
       catch (Exception e)
       {
           Assert.assertTrue(false);
       }


   }

    }
