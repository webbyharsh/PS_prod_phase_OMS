package com.ps.oms.auth.config;

import com.ps.oms.auth.controller.AuthController;
import com.ps.oms.auth.dto.LoginUser;
import com.ps.oms.auth.entities.Role;
import com.ps.oms.auth.entities.User;
import com.ps.oms.auth.repository.UserRepository;

import com.ps.oms.auth.services.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class TokenProviderTests extends AbstractTransactionalTestNGSpringContextTests {
@Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AuthController authController;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserRepository userRepository;
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
    void setUp() {

             user=new User();
            user.setUsername("test5_text@gmail.com");
            user.setPassword(bCryptPasswordEncoder.encode("test"));
            user.setEnabled(true);
            Role role=new Role();
            role.setName("TEST5_USER");
            role.setDescription("TEST5_USER");

            Set<Role> roles=new HashSet<Role>();
            roles.add(role);
            user.setRoles(roles);
            userRepository.save(user);

        loginUser = new LoginUser();
        loginUser.setUsername("test5_text@gmail.com");
        loginUser.setPassword("test");
       user = userService.findOne("test5_text@gmail.com");
        userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), userService.getAuthority(user));
    }
@AfterClass
  void cleanUp()
{
    userRepository.delete(user);
}


    @Test
    public void testGetUsernameFromToken()
    {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        Assert.assertEquals(tokenProvider.getUsernameFromToken(token),loginUser.getUsername());



    }

    @Test
    void testValidateToken()
    {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        Assert.assertTrue(tokenProvider.validateToken(token,userDetails));
    }

    @Test
    void testGetAuthenticationToken()
    {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);

        UsernamePasswordAuthenticationToken UPAT= tokenProvider.getAuthenticationToken( token,authentication ,userDetails) ;
        Assert.assertTrue(UPAT.isAuthenticated());



}
    @Test
    void testValidateRefreshToken()
    {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token =jwtTokenUtil.generateRefreshToken(authentication);
        Assert.assertTrue(tokenProvider.validateRefreshToken(token,userDetails));
    }
    @Test
    public void testGetUsernameFromRefreshToken()
    {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateRefreshToken(authentication);
        Assert.assertEquals(tokenProvider.getUsernameFromRefreshToken(token),loginUser.getUsername());



    }
    @Test
    void testGetAuthenticationRefreshToken()
    {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateRefreshToken(authentication);

        UsernamePasswordAuthenticationToken UPAT= tokenProvider.getAuthenticationRefreshToken( token,authentication ,userDetails) ;
        Assert.assertTrue(UPAT.isAuthenticated());



    }
    @Test
    void testGetAccessTokenFromRefreshToken()
    {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String refreshToken= jwtTokenUtil.generateRefreshToken(authentication);

        String accessToken= tokenProvider.generateAccessFromRefresh(refreshToken) ;
        Assert.assertTrue(tokenProvider.validateToken(accessToken,userDetails));



    }



}
