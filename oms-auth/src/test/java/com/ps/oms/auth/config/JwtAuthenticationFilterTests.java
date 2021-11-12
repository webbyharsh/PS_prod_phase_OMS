package com.ps.oms.auth.config;

import com.ps.oms.auth.dto.LoginUser;
import com.ps.oms.auth.entities.BlackListToken;
import com.ps.oms.auth.entities.Role;
import com.ps.oms.auth.entities.User;
import com.ps.oms.auth.repository.BlackListTokenRepository;
import com.ps.oms.auth.repository.UserRepository;
import com.ps.oms.auth.services.AuthServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.web.filter.OncePerRequestFilter;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@SpringBootTest
public class JwtAuthenticationFilterTests  extends AbstractTransactionalTestNGSpringContextTests{
    @Autowired
    BlackListTokenRepository blackListTokenRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private JwtAuthenticationFilter filter;
    @Autowired
    private AuthServiceImpl userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenProvider jwtTokenUtil;
    LoginUser loginUser;
    User user;
    UserDetails userDetails;

    @BeforeClass
    public void setup() {

             user=new User();
            user.setUsername("test6_text@gmail.com");
        user.setPassword(bCryptPasswordEncoder.encode("test"));
            Role role=new Role();
            user.setEnabled(true);
            role.setName("TEST6_USER");
            role.setDescription("TEST6_USER");
            Set<Role> roles=new HashSet<Role>();
            user.setRoles(roles);
            userRepository.save(user);

        loginUser = new LoginUser();
        loginUser.setUsername("test6_text@gmail.com");
        loginUser.setPassword("test");
        user = userService.findOne("test6_text@gmail.com");
        userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), userService.getAuthority(user));


    }
    @AfterClass
    void cleanUp()
    {
        userRepository.delete(user);
    }
    @Test
    public void testDoFilterInternalSuccess() throws ServletException, IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response= new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        request.addHeader("Authorization",token);

        System.out.println(request.getHeader("Authorization"));
        try {
            this.filter.doFilterInternal(request, response, chain);
            Assert.assertTrue(true);

        }
        catch (Exception e)
        {

            Assert.assertTrue(false);
        }

    }
    @Test
    public void testDoFilterInternalBlackListedRefreshTokenCookie() throws ServletException, IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response= new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String refreshToken = jwtTokenUtil.generateRefreshToken(authentication);
        BlackListToken blackListToken=new BlackListToken();
        blackListToken.setJwtToken(refreshToken);
        blackListTokenRepository.save(blackListToken);
        Cookie cookie1 = new Cookie("refresh-token", refreshToken);
        request.setCookies(cookie1);
        request.setRequestURI("/api/v1/user/refresh-token");
        try {
            this.filter.doFilterInternal(request, response, chain);
            Assert.assertTrue(true);

        }
        catch (Exception e)
        {

            Assert.assertTrue(false);
        }

    }

    @Test
    public void testDoFilterInternalRefreshTokenCookie() throws ServletException, IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response= new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String refreshToken = jwtTokenUtil.generateRefreshToken(authentication);
        Cookie cookie1 = new Cookie("refresh-token", refreshToken);

        request.setCookies(cookie1);
        request.setRequestURI("/api/v1/user/refresh-token");
        try {
            this.filter.doFilterInternal(request, response, chain);
            Assert.assertTrue(true);

        }
        catch (Exception e)
        {

            Assert.assertTrue(false);
        }

    }


    @Test
    public void testDoFilterInternalRefreshTokenException() throws ServletException, IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response= new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String refreshToken = jwtTokenUtil.generateRefreshToken(authentication);
        Cookie cookie1 = new Cookie("refresh-token", refreshToken+"1");

        request.setCookies(cookie1);
        request.setRequestURI("/api/v1/user/refresh-token");
        try {
            this.filter.doFilterInternal(request, response, chain);
            Assert.assertTrue(true);

        }
        catch (Exception e)
        {

            Assert.assertTrue(false);
        }

    }


    @Test
    public void testDoFilterInternalInvalidateToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.getRequestURI().equals("/api/v1/user/invalidate-token");

    }
    @Test
    public void testDoFilterInternalNoSecurityContext() throws ServletException, IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response= new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        final String token = jwtTokenUtil.generateToken(authentication);
        request.addHeader("Authorization",token);

        System.out.println(request.getHeader("Authorization"));
        try {
            this.filter.doFilterInternal(request, response, chain);
            Assert.assertTrue(true);

        }
        catch (Exception e)
        {

            Assert.assertTrue(false);
        }

    }

    @Test
    public void testDoFilterInternalJWTProcessError() throws ServletException, IOException {


        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response= new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        request.addHeader("Authorization","bsncndcdccndcnmd");
        try {
            this.filter.doFilterInternal(request, response,chain);
            Assert.assertTrue(true);

        }
        catch (Exception e)
        {

            Assert.assertTrue(false);

        }






    }


    @Test
    public void testDoFilterInternalIllegalArgument() throws ServletException, IOException {


        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response= new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        request.addHeader("Authorization","");
        try {
            this.filter.doFilterInternal(request, response,chain);
            Assert.assertTrue(true);

        }
        catch (Exception e)
        {

            Assert.assertTrue(false);

        }






    }
    @Test
    public void testDoFilterInternalTokenExpiration() throws ServletException, IOException {


        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response= new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        request.addHeader("Authorization","eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXh0QGdtYWlsLmNvbSIsInJvbGVzIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjI3OTcwODAxLCJleHAiOjE2Mjc5ODg4MDF9.1OPHXIeXoIWnRwrNIlkUuApipXZ1yc0tuQhYuo5O_wQ");
        try {
            this.filter.doFilterInternal(request, response,chain);
            Assert.assertTrue(true);

        }
        catch (Exception e)
        {

            Assert.assertTrue(false);

        }
    }

    @Test
    public void testDoFilterInternalSignatureException() throws ServletException, IOException {
User user1=new User();
user1.setUsername("tempName@gmail.com");

       String epassword=bCryptPasswordEncoder.encode("tempPassword");
       user1.setPassword(epassword);
       user1.setName("TempName");
        Role role=new Role();
        role.setName("TEST6_USER");
        role.setDescription("TEST6_USER");
        Set<Role> roles= new HashSet<Role>();
        roles.add(role);
       user1.setRoles(roles);
        userRepository.save(user1);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response= new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user1.getUsername(),
                        "tempPassword"
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        userRepository.delete(user1);
        request.addHeader("Authorization",token);

        try {
            this.filter.doFilterInternal(request, response,chain);
            Assert.assertTrue(true);
        }
        catch (Exception e)
        {

            Assert.assertTrue(false);
        }

    }
    @Test
    public void testDoFilterInternalNoHeader() throws ServletException, IOException {

        HttpServlet servlet = new HttpServlet() {
        };
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response= new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        List<OncePerRequestFilter> invocations = new ArrayList<>();
        try {
            this.filter.doFilterInternal(request, response,chain);
            Assert.assertTrue(true);

        }
        catch (Exception e)
        {

            Assert.assertTrue(false);

        }
    }

    @Test
    public void testDoFilterInternalBlacklistedToken() throws ServletException, IOException {
        HttpServlet servlet = new HttpServlet() {
        };
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response= new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        List<OncePerRequestFilter> invocations = new ArrayList<>();
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        request.addHeader("Authorization",token);
        BlackListToken blackListToken=new BlackListToken();
        blackListToken.setJwtToken(token);
     blackListTokenRepository.save(blackListToken);

        try {
            this.filter.doFilterInternal(request, response, chain);
            Assert.assertTrue(true);

        }
        catch (Exception e)
        {

            Assert.assertTrue(false);
        }

    }
    @Test
    public void testDoFilterInternalRequestRefresh() throws ServletException, IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response= new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateRefreshToken(authentication);
        request.addHeader("Authorization",token);

        System.out.println(request.getHeader("Authorization"));
        try {
            this.filter.doFilterInternal(request, response, chain);
            Assert.assertTrue(true);

        }
        catch (Exception e)
        {

            Assert.assertTrue(false);
        }

    }

    @Test
    public void testDoFilterInternalNoSecurityContextRefreshToken() throws ServletException, IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response= new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );

        final String refreshToken = jwtTokenUtil.generateRefreshToken(authentication);
        Cookie cookie1 = new Cookie("refresh-token", refreshToken);

        request.setCookies(cookie1);
        request.setRequestURI("/api/v1/user/refresh-token");
        try {
            this.filter.doFilterInternal(request, response, chain);
            Assert.assertTrue(true);

        }
        catch (Exception e)
        {

            Assert.assertTrue(false);
        }

    }

}
