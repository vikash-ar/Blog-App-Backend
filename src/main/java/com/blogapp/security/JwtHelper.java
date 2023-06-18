package com.blogapp.security;

import com.blogapp.enums.ErrorCodes;
import com.blogapp.exceptions.BusinessException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtHelper {
    @Value("${access.token.validity}")
    public long JWT_TOKEN_VALIDITY;
    @Value("${jwt.secret.key}")
    private String secretKey;
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

    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (SignatureException ex) {
            log.error("----JwtHelper:getAllClaimsFromToken:: jwt signature exception----");
            throw new BusinessException(ErrorCodes.INVALID_ACCESS_TOKEN, HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException ex) {
            log.error("----JwtHelper:getAllClaimsFromToken:: token has been expired----");
            throw new BusinessException(ErrorCodes.TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            log.error("----JwtHelper:getAllClaimsFromToken:: {}----", ex.getMessage());
            throw new BusinessException(HttpStatus.UNAUTHORIZED);
        }
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
