package com.shubham.tms.transportrequest.exceptions;

public class InvalidTokenArgumentException extends RuntimeException {
    public InvalidTokenArgumentException(String message, IllegalArgumentException ex) {
        super(message);
    }
}
