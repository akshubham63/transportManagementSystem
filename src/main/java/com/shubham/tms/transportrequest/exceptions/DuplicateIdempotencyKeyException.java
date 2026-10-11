package com.shubham.tms.transportrequest.exceptions;

public class DuplicateIdempotencyKeyException extends RuntimeException {
    public DuplicateIdempotencyKeyException(String message) {
        super(message);
    }
}
