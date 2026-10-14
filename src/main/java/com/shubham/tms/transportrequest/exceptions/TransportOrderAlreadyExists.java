package com.shubham.tms.transportrequest.exceptions;

public class TransportOrderAlreadyExists extends RuntimeException {
    public TransportOrderAlreadyExists(String message) {
        super(message);
    }
}
