package com.blogapp.controllers;

import com.blogapp.model.RequestPage;
import com.blogapp.model.Response;
import com.blogapp.model.dto.UserDto;
import com.blogapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getUserById(@PathVariable("id") long id) {
        Response userDto = userService.getUserById(id);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Response>> getAllUsers(RequestPage pageRequest) {
        List<Response> userDto = userService.getAllUsers(pageRequest);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response> updateUser(@PathVariable("id") long id, @RequestBody UserDto userDto) {
        Response user = userService.updateUser(userDto, id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
