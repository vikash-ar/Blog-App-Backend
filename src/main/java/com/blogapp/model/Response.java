package com.blogapp.model;

import com.blogapp.model.dao.Category;
import com.blogapp.model.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private int id;
    private String title;
    private String description;
    private String content;
    private UserDto user;
    private Long createdAt;
    private Long updatedAt;
    private String imageName;
    private Set<Category> categories;
    private String name;
    private String email;
    private String password;
    private String about;
    private String username;
    private String token;
    public Response(String token) {
        this.token = token;
    }
}
