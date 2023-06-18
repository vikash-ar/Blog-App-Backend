package com.blogapp.exceptions;

import com.blogapp.enums.ErrorCodes;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class BusinessException extends RuntimeException{

    private final HttpStatus status;
    private String message;
    private String code;
    private ErrorCodes errorCode;
    private Object[] args;


    public BusinessException(final ErrorCodes errorCode, final HttpStatus status) {
        super();
        this.status = status;
        this.errorCode = errorCode;
        this.message = errorCode.name();
        this.code = errorCode.getValue();
    }

    public BusinessException(final HttpStatus status) {
        super();
        this.status = status;
    }

    public Object[] getArgs() {
        return args;
    }
}
