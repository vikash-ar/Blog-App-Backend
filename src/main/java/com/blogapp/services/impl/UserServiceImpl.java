package com.blogapp.services.impl;

import com.blogapp.enums.ErrorCodes;
import com.blogapp.exceptions.BusinessException;
import com.blogapp.model.RequestPage;
import com.blogapp.model.Response;
import com.blogapp.model.dao.Role;
import com.blogapp.model.dao.UserDao;
import com.blogapp.model.dto.UserDto;
import com.blogapp.repository.RoleRepository;
import com.blogapp.repository.UserRepository;
import com.blogapp.services.UserService;
import com.blogapp.utils.AppConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ObjectMapper objectMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Response registerUser(UserDto user) {
        if(userRepository.existsByEmail(user.getEmail())) throw new BusinessException(ErrorCodes.USER_ALREADY_PRESENT, HttpStatus.BAD_REQUEST);

        UserDao userDao = objectMapper.convertValue(user, UserDao.class);
        userDao.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findById(AppConstants.NORMAL_USER).get();
        userDao.setRoles(Set.of(role));

        userDao = userRepository.save(userDao);
        log.info("-----UserServiceImpl:registerUser::- new user is registered with email: {}--------", userDao.getEmail());

        return objectMapper.convertValue(userDao, Response.class);
    }

    @SneakyThrows
    @Override
    public Response updateUser(UserDto user, long id) {
        UserDao userDao = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodes.USER_NOT_FOUND, HttpStatus.BAD_REQUEST));

        if(!Objects.isNull(user.getPassword())) {
            userDao.setPassword(passwordEncoder.encode(user.getPassword()));
            log.info("-----UserServiceImpl:updateUser::- user with id: {} has updated its password--", id);
        }
        if(!Objects.isNull(user.getAbout())) {
            log.info("-----UserServiceImpl:updateUser::- user with id: {} has updated its about from: {}--- to: {}--", id, userDao.getAbout(), user.getAbout());
            userDao.setAbout(user.getAbout());
        }

        userDao = userRepository.save(userDao);
        return objectMapper.convertValue(userDao, Response.class);
    }

    @Override
    public Response getUserById(long id) {
        Optional<UserDao> user = userRepository.findById(id);
        if(!user.isPresent()) throw new BusinessException(ErrorCodes.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);

        log.info("-----UserServiceImpl:getUserById::- fetching user with id: {} --------", id);
        return objectMapper.convertValue(user.get(), Response.class);
    }

    @Override
    public List<Response> getAllUsers(RequestPage pageRequest) {
        Sort sort = (pageRequest.getSortDir().equalsIgnoreCase("asc")) ? Sort.by(pageRequest.getSortBy()).ascending() : Sort.by(pageRequest.getSortBy()).descending();
        Pageable page = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sort);
        Page<UserDao> usersPage = userRepository.findAll(page);
        List<UserDao> usersList = usersPage.getContent();
        return usersList.stream().map(userDao -> objectMapper.convertValue(userDao, Response.class)).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(long id) {
        Optional<UserDao> user = userRepository.findById(id);
        if(!user.isPresent()) throw new BusinessException(ErrorCodes.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
        userRepository.deleteById(id);
    }
}
