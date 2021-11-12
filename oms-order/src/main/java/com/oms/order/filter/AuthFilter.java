package com.oms.order.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oms.order.configuration.AuthConfig;
import com.oms.order.dto.ErrorResponse;

import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.*;



import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Order(1)
public class AuthFilter extends OncePerRequestFilter {
    private String SIGNING_KEY = null;
    private Environment environment;
    private AuthConfig authConfig;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public void setAuthConfig(AuthConfig authConfig) {
        this.authConfig = authConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        List<String> excludedPaths = new ArrayList<String>(); // list of paths that for which filter will not work
        excludedPaths.add("/api/v1/order/ack");
        log.info("Authentication filter initiated for api request " + request.getRequestURI());
        if(excludedPaths.contains(request.getRequestURI())){
            filterChain.doFilter(request, response);
        }else{
            SIGNING_KEY = environment.getProperty("jwt.key");
            String accessToken = request.getHeader("Authorization");

            String userIdPassed = request.getHeader("userId");
            String method = request.getMethod();

            try{
                Claims claimsFromToken = getAllClaimsFromToken(accessToken);
                String userId = claimsFromToken.get("id").toString();
                String roles = claimsFromToken.get("roles").toString();
                String[] rolesArr = roles.split(",");
                List<String> listRole = Arrays.asList(rolesArr);
                String username = claimsFromToken.get("sub").toString();
                String isActive = claimsFromToken.get("isActive").toString();
                if(userIdPassed == null){
                    userIdPassed = userId;
                }
                if(userId.equals(userIdPassed)){
                    log.info("Setting up authentication context. Access token is valid");
                    authConfig.setRoles(listRole);
                    authConfig.setUserId(Long.parseLong(userId));
                    authConfig.setUsername(username);
                    authConfig.setIsActive(Boolean.parseBoolean(isActive));
                    log.info("Authentication and setting up auth context from filter done, proceeding with the API call");
                    log.info(authConfig.toString());
                    if(method.equals("GET")){
                        filterChain.doFilter(request, response);
                    }else if(method.equals("PUT") || method.equals("POST") || method.equals("DELETE")){
                        if(authConfig.getIsActive()){
                            filterChain.doFilter(request, response);
                        }else{
                            log.info("The request cannot be completed, as the user is inactive");
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.getWriter().write("The request cannot be completed, as the user is inactive");
                        }
                    }
                }else{
                    log.info("User id passed from headers is not equal to for what user access token is passed for");
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.getWriter().write("User id passed is not equal to what is fetched from token");
                }
            } catch(Exception e){
                log.warn("Some unexpected error might have occurred. The request might be unauthorized 401 for accessing " + request.getRequestURI());
                if(e.getMessage() != null){
                    String message = e.getMessage();
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    ErrorResponse errorResponse = new ErrorResponse("401 UNAUTHORIZED", message);
                    response.setContentType("application/json");
                    response.getWriter().write(convertObjectToJson(errorResponse));
                }else{
                    filterChain.doFilter(request, response);
                }
            }
        }

    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

}
