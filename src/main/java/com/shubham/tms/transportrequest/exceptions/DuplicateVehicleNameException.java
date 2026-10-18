package com.shubham.tms.transportrequest.exceptions;

public class DuplicateVehicleNameException extends RuntimeException {
    public DuplicateVehicleNameException(String message) {
        super(message);
    }
}
