package com.enis.gateways.exceptions;

public class InvalidIpAddressException extends RuntimeException {

    public InvalidIpAddressException(String ip) {

        super("Invalid IP address: " + ip);
    }
}