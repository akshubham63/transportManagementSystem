package com.shubham.tms.transportrequest.enums;

import java.util.Arrays;

public enum TransportRequestStatus {
    CREATED,
    APPROVED,
    REJECTED,
    CANCELLED;

    public static boolean isValid(String value) {
        if(value == null) return false;
        return Arrays.stream(TransportRequestStatus.values())
                .anyMatch(status -> status.name().equals(value));
    }
}
