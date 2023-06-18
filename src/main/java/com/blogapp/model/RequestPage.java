package com.blogapp.model;

import lombok.Data;

@Data
public class RequestPage {
    private long userId;
    private String title;
    private Integer pageNumber = 0;
    private Integer pageSize = 4;
    private String sortBy = "id";
    private String sortDir = "desc";
}
