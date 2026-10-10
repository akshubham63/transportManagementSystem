package com.shubham.tms.transportrequest.exceptions;

public class TransportRequestNotFoundException extends RuntimeException {
    public TransportRequestNotFoundException(String message) {
        super(message);
    }
}
