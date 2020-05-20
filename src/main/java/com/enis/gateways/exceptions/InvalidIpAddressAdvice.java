package com.enis.gateways.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class InvalidIpAddressAdvice {

    @ResponseBody
    @ExceptionHandler(InvalidIpAddressException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String InvalidIpAddressAdvice(InvalidIpAddressException ex) {

        return ex.getMessage();
    }
}