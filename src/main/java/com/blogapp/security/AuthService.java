package com.blogapp.security;

import com.blogapp.enums.ErrorCodes;
import com.blogapp.exceptions.BusinessException;
import com.blogapp.model.Response;
import com.blogapp.model.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {
    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    public AuthService(JwtHelper jwtHelper, UserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        this.jwtHelper = jwtHelper;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    public Response authenticateAndGenerateToken(Token token) {
        authenticate(token.getUsername(), token.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(token.getUsername());
        log.info("-------AuthService:authenticateAndGenerateToken::----token is generated for user: {}-------", token.getUsername());
        String generatedToken = jwtHelper.generateToken(userDetails);
        return new Response(generatedToken);
    }

    private void authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        try {
            authenticationManager.authenticate(authenticationToken);
            log.info("-------AuthService:authenticate::----user Authenticated-------");
        } catch (BadCredentialsException e) {
            log.error("-------AuthService:authenticate::--user is unauthorised");
            throw new BusinessException(ErrorCodes.INVALID_USERNAME_PASSWORD, HttpStatus.UNAUTHORIZED);
        }
    }
}
