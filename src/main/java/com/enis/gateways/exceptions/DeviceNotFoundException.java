package com.enis.gateways.exceptions;

public class DeviceNotFoundException extends RuntimeException {

    public DeviceNotFoundException(Long uid) {
        super("Could not find device " + uid);
    }
}