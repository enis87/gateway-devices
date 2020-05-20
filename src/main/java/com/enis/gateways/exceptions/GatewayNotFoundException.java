package com.enis.gateways.exceptions;

public class GatewayNotFoundException extends RuntimeException {

    public GatewayNotFoundException(String serial) {

        super("Could not find gateway " + serial);
    }
}