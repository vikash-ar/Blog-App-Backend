package com.blogapp.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    @NotNull(message = "name is required")
    @NotEmpty(message = "name is required")
    private String name;
    @NotNull(message = "email is required")
    @Email(message = "enter a valid email")
    private String email;
    @NotNull(message = "password is required")
    @NotEmpty(message = "password is required")
    @Size(min = 4, message = "minimum 4 char password is required")
    private String password;
    private String about;
}
