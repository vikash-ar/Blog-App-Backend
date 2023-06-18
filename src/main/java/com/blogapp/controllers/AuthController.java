package com.blogapp.controllers;

import com.blogapp.enums.ErrorCodes;
import com.blogapp.exceptions.BusinessException;
import com.blogapp.model.Response;
import com.blogapp.model.Token;
import com.blogapp.model.dto.UserDto;
import com.blogapp.security.AuthService;
import com.blogapp.security.JwtHelper;
import com.blogapp.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthController {
    private final UserService userService;
    private final AuthService authService;
    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Response> createToken(@RequestBody Token token) {
        Response generatedToken = authService.authenticateAndGenerateToken(token);
        return new ResponseEntity<>(generatedToken, HttpStatus.CREATED);
    }


    @PostMapping("/register")
    public ResponseEntity<Response> addUser(@Valid @RequestBody UserDto user) {
        Response registeredUser = userService.registerUser(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }
}
