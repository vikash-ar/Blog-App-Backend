package com.blogapp.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExceptionResponse {
    private int status;
    private String error;
    private String message;
    private String path;
    private String code;
    private String type;

    public ExceptionResponse(int status, String error, String message, String path, String code, String type) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.code = code;
        this.type = type;
    }
}
