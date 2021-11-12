package com.ps.oms.auth.config;


import com.ps.oms.auth.repository.BlackListTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Resource(name = "userService")
    private UserDetailsService userDetailsService;
    @Autowired
    private TokenProvider jwtTokenUtil;
    @Autowired
    private FilterExceptionMessage filterExceptionMessage;

    @Autowired
    private BlackListTokenRepository blackListTokenRepository;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String accessToken = null;
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null && (request.getRequestURI().equals("/api/v1/user/refresh-token"))){
                refreshToken = getTokenFromCookie("refresh-token", cookies);
        } else{
          accessToken = request.getHeader("Authorization");
        }
        String username = null;
        String authToken = null;
        if (accessToken != null) {
              authToken = accessToken;
            try {
                if(blackListTokenRepository.findByJwtToken(accessToken) != null){
                    log.warn("Token is blacklisted");
                    filterExceptionMessage.setMessage("JWT_INVALID | The access token is already invalidated. Generate new token");
                }else{
                    log.info("Token is not blacklisted, proceed with authentication");
                    username = jwtTokenUtil.getUsernameFromToken(authToken);
                }

            } catch (IllegalArgumentException e) {
                log.error("An error occurred while fetching Username from Token", e);
                filterExceptionMessage.setMessage("JWT_FETCH_ERR : An error occured while fetching username from token");
            } catch (ExpiredJwtException e) {
                log.warn("The token has expired", e);
                filterExceptionMessage.setMessage("JWT_EXPIRED : The token has expired");
            } catch(SignatureException e){
                log.error("Authentication Failed. Username or Password not valid.");
                filterExceptionMessage.setMessage("JWT_AUTH_FAIL : Authentication failed. Username or password not valid!");
            } catch (Exception e){
                log.error("Error occured while processing JWT Token");
                filterExceptionMessage.setMessage("JWT_ERR : Error occured while processing JWT Token. It might be malformed");
            }
        }
        else if(refreshToken != null) {
            authToken = refreshToken;
            try {
                if(blackListTokenRepository.findByJwtToken(refreshToken) != null){
                    log.warn("Refresh Token is blacklisted");
                    filterExceptionMessage.setMessage(" Refresh JWT_INVALID | The refresh token is already invalidated. Generate new token");
                }else {
                    log.info("Refresh Token is not blacklisted, proceed with authentication");
                    username = jwtTokenUtil.getUsernameFromRefreshToken(authToken);
                }

            } catch (Exception e) {
                log.info("Some unexpected error occured while processing Refresh token");
                filterExceptionMessage.setMessage("REFRESH_TOKEN_ERR :Some unexpected error occured while processing JWT Token " + e.getMessage());
            }
        }
        else {
            filterExceptionMessage.setMessage("NO_AUTHORIZATION_IDENTIFIER: No Authorization Header or cookies present.");
            log.warn("No Authorization header or cookies present");
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (accessToken != null && (jwtTokenUtil.validateToken(authToken, userDetails))) {
                UsernamePasswordAuthenticationToken authentication = jwtTokenUtil.getAuthenticationToken(authToken, SecurityContextHolder.getContext().getAuthentication(), userDetails);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                log.info("authenticated user " + username + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            else if(refreshToken != null && (jwtTokenUtil.validateRefreshToken(authToken, userDetails))) {
                UsernamePasswordAuthenticationToken authentication = jwtTokenUtil.getAuthenticationRefreshToken(authToken, SecurityContextHolder.getContext().getAuthentication(), userDetails);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                log.info("authenticated user " + username + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }

    private String getTokenFromCookie(String cookieName, Cookie[] cookies){
        for(Cookie c : cookies){
            if(c.getName().equals(cookieName)){
                return c.getValue();
            }
        }
        return null;
    }

}