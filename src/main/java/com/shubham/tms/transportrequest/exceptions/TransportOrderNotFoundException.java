package com.shubham.tms.transportrequest.exceptions;

public class TransportOrderNotFoundException extends RuntimeException {
    public TransportOrderNotFoundException(String message) {
        super(message);
    }
}
