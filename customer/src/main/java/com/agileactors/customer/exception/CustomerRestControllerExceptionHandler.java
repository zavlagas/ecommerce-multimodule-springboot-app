package com.agileactors.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class CustomerRestControllerExceptionHandler {


    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    private ErrorResponse handleServer500Error(Exception exception, WebRequest request) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }


    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    private ErrorResponse handleServerRuntimeException(Exception exception, WebRequest request) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }


}
