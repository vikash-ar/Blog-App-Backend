package com.blogapp.security;

import com.blogapp.enums.ErrorCodes;
import com.blogapp.exceptions.BusinessException;
import com.blogapp.model.dao.UserDao;
import com.blogapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserDao> user = userRepository.findByEmail(username);

        if(user.isPresent()) return user.get();

        log.error("-------CustomUserDetailsService:loadUserByUsername::--token user is not present in db");
        throw new BusinessException(ErrorCodes.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
    }
}
