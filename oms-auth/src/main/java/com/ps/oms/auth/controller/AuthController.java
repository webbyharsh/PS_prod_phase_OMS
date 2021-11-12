package com.ps.oms.auth.controller;

import com.ps.oms.auth.config.TokenProvider;
import com.ps.oms.auth.dto.*;
import com.ps.oms.auth.entities.BlackListToken;
import com.ps.oms.auth.entities.RefreshAccessToken;
import com.ps.oms.auth.repository.BlackListTokenRepository;
import com.ps.oms.auth.repository.RefreshAccessTokenRepository;
import com.ps.oms.auth.repository.UserRepository;
import com.ps.oms.auth.services.AuthServiceImpl;
import com.ps.oms.auth.services.IAuthService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class AuthController{


    @Value("${jwt.refreshtoken.token.validity}")
    public long refreshTokenValidity;
	
	@Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @Autowired
    private IAuthService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    private RefreshAccessTokenRepository refreshAccessTokenRepository;

    @Autowired
    private BlackListTokenRepository blackListTokenRepository;

    //end point to get JWT token for registered username and password
    @PostMapping (value = "/generate-token")
    public ResponseEntity<AccessToken> generateToken(@RequestBody LoginUser loginUser) throws AuthenticationException {
   	log.info("Authentication in process");
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String accessToken = jwtTokenUtil.generateToken(authentication);
        if(accessToken.equals("-1"))return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        final String refreshToken = jwtTokenUtil.generateRefreshToken(authentication);
        ResponseCookie refreshJwtResponseCookie = ResponseCookie.from("refresh-token", refreshToken)
                .path("/")
                .httpOnly(true)
                .maxAge(refreshTokenValidity)
                .sameSite("None")
                .secure(true)
                .build();
        log.info("Successfully authenticated and token generated");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, refreshJwtResponseCookie.toString());
        Date refreshTokenExpiryDate = jwtTokenUtil.getExpirationDateFromRefreshToken(refreshToken);
        refreshAccessTokenRepository.save(new RefreshAccessToken(refreshToken, accessToken, refreshTokenExpiryDate));
        return ResponseEntity.ok().headers(httpHeaders).body(new AccessToken(accessToken));
    }

    //to refresh access token ( ONLY HAS TO BE CALLED FROM CLIENT )
    @GetMapping(value="/refresh-token")
    public ResponseEntity<AccessToken> generateAccessTokenFromRefreshToken(@CookieValue(value="refresh-token") String authorization) {
        String refreshToken = authorization;
        RefreshAccessToken refreshAccessToken = refreshAccessTokenRepository.findByRefreshToken(refreshToken);
        String accessToken = null;
        if(refreshAccessToken != null){
         accessToken = refreshAccessToken.getAccessToken();
         try{
             jwtTokenUtil.getExpirationDateFromToken(accessToken);
         } catch (ExpiredJwtException accessTokenExpiredException){
             accessToken = jwtTokenUtil.generateAccessFromRefresh(refreshToken);
             refreshAccessToken.setAccessToken(accessToken);
             refreshAccessTokenRepository.save(refreshAccessToken);
         }
        }

        return ResponseEntity.ok().body(new AccessToken(accessToken));
    }


    //end point to authenticate any request containing JWT token in the header
    @GetMapping(value="/authenticate")
    public ResponseEntity<AuthenticationResponse> userAuthenticate(@RequestHeader(value="Authorization") String authorization){
        log.info("In controller authenticate");
        log.info("Request is authenticated");
        String accessToken = authorization;
        String username = jwtTokenUtil.getUsernameFromToken(accessToken);
        String id = jwtTokenUtil.getAllClaimsFromToken(accessToken).get("id").toString();
        String isActive = jwtTokenUtil.getAllClaimsFromToken(accessToken).get("isActive").toString();
        Long userId = Long.parseLong(id);
        return ResponseEntity.status(HttpStatus.OK).body(new AuthenticationResponse(userId, true, username, Boolean.parseBoolean(isActive)));
    }

    //end point to authorize any request
    @GetMapping(value="/authorize")
    public ResponseEntity<AuthorizationResponse> userAuthorize(@RequestHeader(value="Authorization") String authorization){
        log.info("Request is authenticated");
        log.info("In controller auth");
        String accessToken = authorization;
        log.info("JWT access token " + accessToken);
        log.info("Getting claims from the token");
        String roles = jwtTokenUtil.getAllClaimsFromToken(accessToken).get("roles").toString();
        String[] rolesArr = roles.split(",");
        List<String> listRole = Arrays.asList(rolesArr);
        return ResponseEntity.status(HttpStatus.OK).body(new AuthorizationResponse(listRole));
    }

    //end point to invalidate access token ( ONLY HAS TO BE CALLED FROM CLIENT )
    @PostMapping(value="/invalidate-token")
    public ResponseEntity<String> logoutUser(@RequestHeader(value="Authorization") String jwtToken,
                                             @CookieValue(value="refresh-token") String refreshToken){
        log.info("In logout controller method");
        if(jwtToken != null){
            Date jwtExpiration = jwtTokenUtil.getExpirationDateFromToken(jwtToken);
            authService.saveBlackListToken(new BlackListToken(jwtToken, jwtExpiration));
        }
        if(refreshToken != null){
            Date refreshJwtExpiration = jwtTokenUtil.getExpirationDateFromRefreshToken(refreshToken);
            authService.saveBlackListToken(new BlackListToken(refreshToken, refreshJwtExpiration));
        }
        return ResponseEntity.status(HttpStatus.OK).body("Token(s) invalidated");
    }
}