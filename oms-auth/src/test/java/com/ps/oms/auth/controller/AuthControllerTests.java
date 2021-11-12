package com.ps.oms.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ps.oms.auth.config.TokenProvider;
import com.ps.oms.auth.dto.*;
import com.ps.oms.auth.entities.RefreshAccessToken;

import com.ps.oms.auth.entities.Role;
import com.ps.oms.auth.entities.User;

import com.ps.oms.auth.repository.BlackListTokenRepository;

import com.ps.oms.auth.repository.RefreshAccessTokenRepository;
import com.ps.oms.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.mock.web.MockHttpServletRequest;

import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;


import javax.persistence.Access;
import java.util.ArrayList;

import java.util.HashSet;

import java.util.List;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests extends AbstractTransactionalTestNGSpringContextTests {
    String generateTokenUrl="/api/v1/user/generate-token";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AuthController authController;

    @Autowired
    private RefreshAccessTokenRepository refreshAccessTokenRepository;

    @Autowired
    private UserRepository userRepository;

    User user;


    @Autowired
    private BlackListTokenRepository blackListTokenRepository;


    private String validJwtTokenForTest = null;
    private String validRefreshTokenForTest = null;
    private String usernameForTest = "test_text@gmail.com";
    private String passwordForTest = "test";

    @Autowired
    private TokenProvider jwtTokenUtil;

    @Autowired
    MockHttpServletRequest request;
    @Autowired
    private AuthenticationManager authenticationManager;
    RefreshAccessToken refreshAccessToken=new RefreshAccessToken();
    @BeforeClass
    @Rollback
    public void setup(){
        user=new User();
        user.setUsername("test_text@gmail.com");
        user.setPassword(bCryptPasswordEncoder.encode("test"));
        user.setIsActive(true);
        user.setEnabled(true);
        Role role=new Role();
        role.setName("TEST_USER");
        role.setDescription("TEST_USER");
        Set<Role> roles=new HashSet<Role>();
        roles.add(role);
        user.setRoles(roles);

        userRepository.save(user);
        LoginUser loginUser=new LoginUser();
        loginUser.setUsername("test_text@gmail.com");
        loginUser.setPassword("test");
        ResponseEntity<AccessToken> s=authController.generateToken(loginUser);
        validJwtTokenForTest=s.getBody().getJwtAccessToken();
        validRefreshTokenForTest=s.getHeaders().get("Set-Cookie").get(0).split(";")[0].substring(14);
        System.out.println(validRefreshTokenForTest);

    }
    @AfterClass
    void cleanUp()
    {
        userRepository.delete(user);
        refreshAccessTokenRepository.delete(refreshAccessToken);
    }
    @Test
    void testStatusBadRequestIfNoRequestBody() throws Exception {
        mockMvc.perform(post(generateTokenUrl).contentType(APPLICATION_JSON)).andExpect(status().isBadRequest());  }







    @Test
    void testExceptionIfBadCredentials() throws Exception {
        LoginUser loginUser=new LoginUser();
        loginUser.setUsername("test_text@gmail.com");
        loginUser.setPassword("test123");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(loginUser);
        MvcResult response = mockMvc.perform(post(generateTokenUrl).contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{'error':'400 BAD_REQUEST','message':'Bad credentials'}")).andReturn();
        System.out.println(response.getResponse());
    }

    //should be second last


    @Test(dependsOnMethods = {"testAuthenticateFunction"})
    public void testRefreshTokenFunctionIfPreviousNotExpired(){
        ResponseEntity<AccessToken> response = authController.generateAccessTokenFromRefreshToken(validRefreshTokenForTest);
        RefreshAccessToken refreshAccessToken = refreshAccessTokenRepository.findByRefreshToken(validRefreshTokenForTest);
        Assert.assertEquals(validJwtTokenForTest, refreshAccessToken.getAccessToken());
    }


    @Test
    @Ignore
    void testInvalidateTokenEndpoint() throws Exception{
        MvcResult invalidateResponse = mockMvc.perform(post("/api/v1/user/invalidate-token").contentType(APPLICATION_JSON)
                        .header("AccessToken", validJwtTokenForTest).header("RefreshToken", validRefreshTokenForTest))
                .andExpect(status().isOk()).andReturn();
    }



    @Test(dependsOnMethods = {"testExceptionIfBadCredentials"})
    public void testAuthenticateFunction(){
        ResponseEntity<AuthenticationResponse> response = authController.userAuthenticate(validJwtTokenForTest);
        Assert.assertEquals(response.getBody().getUsername(), usernameForTest);
    }

    @Test
    @Ignore
    public void testAuthenticateEndpoint() throws Exception{
        mockMvc.perform(get("/api/v1/user/authenticate").contentType(APPLICATION_JSON)
                        .header("Authorization", "Bearer " + validJwtTokenForTest))
                .andExpect(status().isOk());
    }

    @Test
    public void testAuthorizeFunction(){
        ResponseEntity<AuthorizationResponse> response = authController.userAuthorize(validJwtTokenForTest);
        List<String> actualRoles = new ArrayList<String>();
        // actualRoles.add("ROLE_ADMIN");
        actualRoles.add("ROLE_TEST_USER");
        Assert.assertEquals(response.getBody().getListRoles(), actualRoles);
    }

    @Test
    @Ignore
    public void testAuthorizeEndpoint() throws Exception{
        mockMvc.perform(get("/api/v1/user/authorize").contentType(APPLICATION_JSON)
                        .header("Authorization",   validJwtTokenForTest))
                .andExpect(status().isOk());
    }


    @Test
    public void testInvalidateTokenFunction(){
        ResponseEntity<String> response = authController.logoutUser(validJwtTokenForTest, validRefreshTokenForTest);
        Assert.assertEquals(response.getStatusCodeValue(), 200);
    }


    @Test(dependsOnMethods = {"testExceptionIfBadCredentials"})
    public void testIfUserNotEnabled(){
        User user = userRepository.findByUsername("test_text@gmail.com");
        user.setEnabled(false);
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername("test_text@gmail.com");
        loginUser.setPassword("test");
        ResponseEntity<AccessToken> response = authController.generateToken(loginUser);
        Assert.assertEquals(response.getStatusCodeValue(), 401);

    }


}