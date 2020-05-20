package com.enis.gateways.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GatewayNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(GatewayNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String GatewayNotFoundAdvice(GatewayNotFoundException ex) {
        return ex.getMessage();
    }
}