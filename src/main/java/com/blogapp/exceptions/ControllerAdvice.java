package com.blogapp.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;

@RestControllerAdvice
@Slf4j
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestControllerAdvice.class);

    private final MessageSource messageSource;

    private final Locale currentLocale = LocaleContextHolder.getLocale();

    @Autowired
    public ControllerAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<?> sqlException(SQLException exception) {
        if (exception.getMessage().contains("duplicate"))
            return new ResponseEntity<>("User with this email already exist", HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * show validation errors
     * @param ex the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());

        Stream<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage());

        body.put("message", errors.findFirst().get());

        return new ResponseEntity<>(body, headers, status);
    }



    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionResponse> handleBusinessException(final BusinessException exc,
                                                                     final WebRequest request) {
        String errorMessage = exc.getErrorCode().name();
        log.info("-------errormessage : {}-----",errorMessage);
        try {
            errorMessage = messageSource.getMessage(errorMessage, exc.getArgs(), currentLocale);
        } catch (Exception e) {
            LOGGER.info(" ------- Rest Controller Advice Exception {} ------------", errorMessage);
        }
        return ResponseEntity.status(Objects.nonNull(exc.getStatus()) ? exc.getStatus() : HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(exc.getStatus().value(), exc.getStatus().getReasonPhrase(), errorMessage,
                        ((ServletWebRequest) request).getRequest().getServletPath(), exc.getCode(), exc.getMessage()));
    }
}
