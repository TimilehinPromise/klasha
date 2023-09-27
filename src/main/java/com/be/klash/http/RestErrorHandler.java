package com.be.klash.http;

import com.be.klash.models.ErrorResponse;
import exception.ResourceNotFoundException;
import exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestErrorHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleResourceException(ResourceNotFoundException exception) {
        ErrorResponse response = (exception.getErrorResponse() == null) ? new ErrorResponse(exception.getCode(), exception.getMessage()) : exception.getErrorResponse();
        return response;
    }


    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleValidationException(ValidationException exception) {
        ErrorResponse response = (exception.getErrorResponse() == null) ? new ErrorResponse(exception.getCode(), exception.getMessage()) : exception.getErrorResponse();
        return response;
    }
}
