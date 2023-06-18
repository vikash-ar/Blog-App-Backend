package com.blogapp.utils;

import com.blogapp.enums.ErrorCodes;
import com.blogapp.exceptions.BusinessException;
import com.blogapp.model.dao.UserDao;
import com.blogapp.repository.UserRepository;
import com.blogapp.security.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class JwtUtils {
    private final JwtHelper jwtHelper;
    private final UserRepository userRepository;
    @Autowired
    public JwtUtils(JwtHelper jwtHelper, UserRepository userRepository) {
        this.jwtHelper = jwtHelper;
        this.userRepository = userRepository;
    }

    public long getUserIdFromToken(String token) {
        String username = jwtHelper.getUsernameFromToken(token);
        Optional<UserDao> user = userRepository.findByEmail(username);
        if(user.isPresent()) return user.get().getId();

        log.info("-------JwtUtils:getUSerIdFromToken::---user contained in token does not exists-----");
        throw new BusinessException(ErrorCodes.USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
    }
}
