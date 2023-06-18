package com.blogapp.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDto {
    @NotNull(message = "title is required")
    @NotEmpty(message = "title can't be empty")
    private String title;
    @NotNull(message = "content is required")
    private String content;
}
