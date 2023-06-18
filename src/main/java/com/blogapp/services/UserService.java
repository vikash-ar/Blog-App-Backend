package com.blogapp.services;

import com.blogapp.model.RequestPage;
import com.blogapp.model.Response;
import com.blogapp.model.dto.UserDto;

import java.util.List;

public interface UserService {
    Response registerUser(UserDto user);
    Response updateUser(UserDto user, long id);
    Response getUserById(long id);
    List<Response> getAllUsers(RequestPage pageRequest);
    void deleteUser(long id);
}
