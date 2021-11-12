package com.ps.oms.auth.services;

import java.util.*;

import com.ps.oms.auth.entities.BlackListToken;
import com.ps.oms.auth.repository.BlackListTokenRepository;
import com.ps.oms.auth.repository.RefreshAccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ps.oms.auth.repository.UserRepository;
import com.ps.oms.auth.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service(value = "userService")
public class AuthServiceImpl implements UserDetailsService, IAuthService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlackListTokenRepository blackListTokenRepository;

    @Autowired
    private RefreshAccessTokenRepository refreshAccessTokenRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null){
            log.error("Invalid username and password provided");
            throw new UsernameNotFoundException("Invalid username or password.");

        }
        log.info("Username exists in the database");
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority(user));
    }

    public Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role ->
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()))
        );
        return authorities;
    }

    @Override
    public User findOne(String username) {
        return userRepository.findByUsername(username);
    }

    public BlackListToken saveBlackListToken(BlackListToken token){
        return blackListTokenRepository.save(token);
    }

    public Long getUserIdFromUserName(String username) {

        return userRepository.findUserIdFromUserName(username);
    }

    //schedule for every 30 minutes
    @Scheduled(fixedDelay =  30*60*1000)
    @Transactional
    public void scheduleFixedDelayTask(){
        log.info("Schedule deletion of expired blacklisted tokens and refresh access tokens initiated");
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        try{
            log.info("Deleting expired tokens...");
            blackListTokenRepository.deleteByExpirationTimeBefore(now);
            refreshAccessTokenRepository.deleteByRefreshTokenExpirationDateBefore(now);
            log.info("Deleted expired tokens..." );
        } catch (Exception e){
            log.error("Error occurred while deleting token");
        }
    }
}