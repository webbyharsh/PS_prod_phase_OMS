package com.ps.oms.auth.config;

import com.ps.oms.auth.entities.User;
import com.ps.oms.auth.services.AuthServiceImpl;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenProvider implements Serializable {

    @Value("${jwt.token.validity}")
    public long tokenValidity;

    @Value("${jwt.refreshtoken.token.validity}")
    public long refreshTokenValidity;

    @Value("${jwt.signing.key}")
    public String accessSigningKey;

    @Value("${jwt.refreshtoken.signing.key}")
    public String refreshSigningKey;

    @Value("${jwt.authorities.key}")
    public String authoritiesKey;

    @Autowired
    AuthServiceImpl authService;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(accessSigningKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public Date getExpirationDateFromRefreshToken(String token) {
        return getClaimFromRefreshToken(token, Claims::getExpiration);
    }

    public String getAuthoritiesFromRefreshToken(String refreshToken) {
        final Claims claims = getAllClaimsFromRefreshToken(refreshToken);
        String authorities = claims.get(authoritiesKey).toString();
        return authorities;
    }

    public String getUsernameFromRefreshToken(String refreshToken) {
        return getClaimFromRefreshToken(refreshToken, Claims::getSubject);
    }

    public <T> T getClaimFromRefreshToken(String refreshToken, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromRefreshToken(refreshToken);
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromRefreshToken(String refreshToken) {
        return Jwts.parser()
                .setSigningKey(refreshSigningKey)
                .parseClaimsJws(refreshToken)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean isRefreshTokenExpired(String token) {
        final Date expiration = getExpirationDateFromRefreshToken(token);
        return expiration.before(new Date());
    }

    public String generateRefreshToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(authoritiesKey, authorities)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity*1000))
                .signWith(SignatureAlgorithm.HS256, refreshSigningKey)
                .compact();
    }

    public String generateToken(Authentication authentication) {
        User user1 = authService.findOne(authentication.getName());
        Long userId = user1.getUserId();
        Boolean isActive = user1.getIsActive();
        Boolean isEnabled = user1.isEnabled();
        if(isEnabled){
            String authorities = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            Map<String,Object> claims = new HashMap<>();
            claims.put("id", userId);
            claims.put("sub",authentication.getName());
            claims.put("isActive", isActive);

            return Jwts.builder()
                    .setClaims(claims)
                    .claim(authoritiesKey, authorities)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + tokenValidity*1000))
                    .signWith(SignatureAlgorithm.HS256, accessSigningKey)
                    .compact();
        }else{
            return "-1";
        }
    }

    public String generateAccessFromRefresh(String refreshToken) {
        log.info("generate access from refresh method called");
        final String username = getUsernameFromRefreshToken(refreshToken);
        User user = authService.findOne(username);
        Long userId = user.getUserId();
        Map<String,Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("sub",username);
        claims.put("isActive", user.getIsActive());
        final String authorities = getAuthoritiesFromRefreshToken(refreshToken);
        return Jwts.builder()
                .setClaims(claims)
                .claim(authoritiesKey, authorities)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidity*1000))
                .signWith(SignatureAlgorithm.HS256, accessSigningKey)
                .compact();
    }

    public Boolean validateRefreshToken(String refreshToken, UserDetails userDetails) {
        final String username = getUsernameFromRefreshToken(refreshToken);
        log.info("Refresh Token validated");
        return (username.equals(userDetails.getUsername()) && !isRefreshTokenExpired(refreshToken));
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        log.info("Token validated");
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    UsernamePasswordAuthenticationToken getAuthenticationToken(final String token, final Authentication existingAuth, final UserDetails userDetails) {

        final JwtParser jwtParser = Jwts.parser().setSigningKey(accessSigningKey);

        final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);

        final Claims claims = claimsJws.getBody();

        final Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(authoritiesKey).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    UsernamePasswordAuthenticationToken getAuthenticationRefreshToken(final String token, final Authentication existingAuth, final UserDetails userDetails){
        final JwtParser jwtParser = Jwts.parser().setSigningKey(refreshSigningKey);

        final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);

        final Claims claims = claimsJws.getBody();

        final Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(authoritiesKey).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

}